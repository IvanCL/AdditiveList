package com.icl.additivelist.usescase.products

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.icl.additivelist.R
import com.icl.additivelist.databinding.ActivityProductsBinding
import java.io.File

class ProductsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductsBinding
    private val viewModel: ProductAnalysisViewModel by viewModels()
    private var pendingPhotoUri: Uri? = null

    // Camera capture contract
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            pendingPhotoUri?.let { uri ->
                decodeBitmap(uri)?.let { viewModel.analyzeImage(it) }
            }
        }
    }

    // Gallery picker contract
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { decodeBitmap(it)?.let { bmp -> viewModel.analyzeImage(bmp) } }
    }

    // Camera permission contract
    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) launchCamera()
            else Toast.makeText(this, getString(R.string.camera_permission_denied), Toast.LENGTH_SHORT).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupButtons()
        observeViewModel()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupButtons() {
        binding.btnCamera.setOnClickListener { checkCameraPermissionAndLaunch() }
        binding.btnGallery.setOnClickListener { pickImage.launch("image/*") }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { loading ->
            binding.loadingLayout.visibility = if (loading) View.VISIBLE else View.GONE
            binding.btnCamera.isEnabled = !loading
            binding.btnGallery.isEnabled = !loading
        }

        viewModel.analysisResult.observe(this) { result ->
            result ?: return@observe
            navigateToResult(result)
            viewModel.clearResult()
        }

        viewModel.error.observe(this) { error ->
            error ?: return@observe
            Toast.makeText(this, getString(R.string.ocr_error), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkCameraPermissionAndLaunch() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> launchCamera()
            else -> requestCameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private fun launchCamera() {
        val imageFile = File.createTempFile("product_", ".jpg", cacheDir)
        val uri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", imageFile)
        pendingPhotoUri = uri
        takePicture.launch(uri)
    }

    private fun decodeBitmap(uri: Uri): Bitmap? {
        return try {
            contentResolver.openInputStream(uri)?.use { stream ->
                BitmapFactory.decodeStream(stream)
            }
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.ocr_error), Toast.LENGTH_SHORT).show()
            null
        }
    }

    private fun navigateToResult(result: ProductAnalysisResult) {
        val intent = Intent(this, ProductResultActivity::class.java).apply {
            putExtra(ProductResultActivity.EXTRA_VERDICT, result.verdict)
            putParcelableArrayListExtra(ProductResultActivity.EXTRA_ADDITIVES, ArrayList(result.detectedAdditives))
            putParcelableArrayListExtra(ProductResultActivity.EXTRA_INGREDIENTS, ArrayList(result.detectedIngredients))
        }
        startActivity(intent)
    }
}
