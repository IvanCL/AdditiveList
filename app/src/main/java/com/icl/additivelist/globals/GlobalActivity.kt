package com.icl.additivelist.globals

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import com.icl.additivelist.R
import com.icl.additivelist.databinding.ActivityFindAdditivesBinding
import com.icl.additivelist.models.Additive
import com.icl.additivelist.usescase.additives.AdditiveAdapter
import com.icl.additivelist.usescase.additives.AdditivesActivity
//import kotlinx.android.synthetic.main.activity_find_additives.*

abstract class GlobalActivity : AppCompatActivity() {

    lateinit var additivesList: ArrayList<Additive>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Init Food Additives", localClassName)
    }


    // region nav

    fun loadFragment() {
        /*additivesList = getPosts()
        val binding = ActivityFindAdditivesBinding(setContentView(R.layout.activity_find_additives))

        containerAdditives.layoutManager =
            LinearLayoutManager(this)
        var additiveAdapter = AdditiveAdapter(additivesList, this@GlobalActivity)
        containerAdditives.adapter = additiveAdapter**/
/*        navView.setNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.op1 -> {
                    Log.d("OP1", "POSTS")
                    var postsAdapter = PostsAdapter(postsList, this@GlobalActivity)
                    container.adapter = postsAdapter
                }
                R.id.op2 -> {
                    Log.d("OP2", "ALBUMS")
                    var albumsAdapter = AlbumsAdapter(albumsList, this@GlobalActivity)
                    container.adapter= albumsAdapter
                }
                R.id.op3 -> {
                    Log.d("OP3", "USERS")
                    var usersAdapter = UsersAdapter(usersList, this@GlobalActivity)
                    container.adapter = usersAdapter
                }
                R.id.op4 ->{
                    Log.d("OP4", "TUTORIAL")
                    var intent = Intent(this@GlobalActivity, TutorialActivity::class.java)
                    startActivity(intent)
                }
            }
            false

        }*/

    }

    // endregion nav

    // region data

    fun getPosts(): ArrayList<Additive> {
        additivesList = arrayListOf()
        var post = Additive("1", "E-120", "Carmin", "Carmin a base de matar insectos y triturarlos", "Dar color rojo", "Da asco y mueren insectos", "No vegano")
        var post2 = Additive("1", "E-120", "Carmin", "Carmin a base de matar insectos y triturarlos", "Dar color rojo", "Da asco y mueren insectos", "No vegano")
        var post3 = Additive("1", "E-120", "Carmin", "Carmin a base de matar insectos y triturarlos", "Dar color rojo", "Da asco y mueren insectos", "No vegano")
        var post4 = Additive("1", "E-120", "Carmin", "Carmin a base de matar insectos y triturarlos", "Dar color rojo", "Da asco y mueren insectos", "No vegano")
        var post5 = Additive("1", "E-120", "Carmin", "Carmin a base de matar insectos y triturarlos", "Dar color rojo", "Da asco y mueren insectos", "No vegano")
        var post6 = Additive("1", "E-120", "Carmin", "Carmin a base de matar insectos y triturarlos", "Dar color rojo", "Da asco y mueren insectos", "No vegano")

        additivesList.add(post)
        additivesList.add(post2)
        additivesList.add(post3)
        additivesList.add(post4)
        additivesList.add(post5)
        additivesList.add(post6)

        return additivesList
    }



    // endregion data
}

