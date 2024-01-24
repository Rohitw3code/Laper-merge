package com.lapperapp.laper.ui.old.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.lapperapp.laper.BuildConfig
import com.lapperapp.laper.Categories.*
import com.lapperapp.laper.R
import com.lapperapp.laper.project.ProjectRequestActivity
import com.lapperapp.laper.query.QuerySubActivity
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    var db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var devAdapter: DeveloperAdapter
    var userRef = db.collection("users")
    var articleRef = db.collection("articles")
    var expertRef = db.collection("experts")
    var techRef = db.collection("tech")
    var adminRef = db.collection("admin")
    val database = Firebase.database
    val tokenRef = database.getReference("token")
    var devData = ArrayList<DevModel>()
    var data = ArrayList<CategoryModel>()
    private lateinit var adapter: CategoryAdapter
    val imageList = ArrayList<SlideModel>()

    //    private lateinit var imageSlider: ImageSlider
    private lateinit var userImage2: CircleImageView
    private lateinit var querySubmit: CardView
    private lateinit var articleTitle: TextView
    private lateinit var articleImage: ImageView
    private lateinit var cateRecyclerView: RecyclerView
    private lateinit var viewAllDevelopers: LinearLayout
    private lateinit var newproject: CardView
    private lateinit var categories: CardView
    private lateinit var updateCard: CardView
    private lateinit var updateText: TextView
    private lateinit var noticeCard: CardView
    private lateinit var noticeText: TextView

    @SuppressLint("NotifyDataSetChanged", "MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        userImage2 = view.findViewById(R.id.home_user_image2)
        newproject = view.findViewById(R.id.home_new_project)
        querySubmit = view.findViewById(R.id.home_ask_question)
        articleTitle = view.findViewById(R.id.home_article_title)
        articleImage = view.findViewById(R.id.home_article_image)
        cateRecyclerView = view.findViewById(R.id.frag_home_category)
        categories = view.findViewById(R.id.cat_card_home)
        viewAllDevelopers = view.findViewById(R.id.view_all_developers)
        updateCard = view.findViewById(R.id.update_card)
        updateText = view.findViewById(R.id.update_text)
        noticeCard = view.findViewById(R.id.notice_card)
        noticeText = view.findViewById(R.id.notice_text)

        data = ArrayList()
        adapter = CategoryAdapter(data)
        cateRecyclerView.layoutManager = GridLayoutManager(context, 3)
        cateRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        updateCard.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data =
                Uri.parse("https://play.google.com/store/apps/details?id=com.lapperapp.laper")
            startActivity(openURL)
        }

        querySubmit.setOnClickListener {
            val intent = Intent(context, QuerySubActivity::class.java)
            startActivity(intent)
        }

        categories.setOnClickListener {
            val intent = Intent(context, AllCategoryActivity::class.java)
            startActivity(intent)
        }

        newproject.setOnClickListener {
            val intent = Intent(context, ProjectRequestActivity::class.java)
            startActivity(intent)
        }

        viewAllDevelopers.setOnClickListener {
            val intent = Intent(context, ViewAllExpertsActivity::class.java)
            startActivity(intent)
        }

        setToken()
        fetchUserDetail()


        userRef.document(firebaseAuth.uid as String)
            .get().addOnSuccessListener { doc ->
                var m = doc.getString("userType")
                if (m.equals("developer")) {
                    expertMode()
                }
            }

        fetchArticle()
        getTech()
        checkVisibility()


        return view

    }

    fun checkVisibility() {
        adminRef.document("popup")
            .get().addOnSuccessListener { doc ->
                val showUpdate = doc.getBoolean("showUpdatePopUp") as Boolean
                val cvn = doc.getString("currentVersionName") as String
                val utext = doc.getString("updateText") as String
                updateText.text = utext
                if (showUpdate && cvn.equals(BuildConfig.VERSION_NAME)) {
                    updateCard.visibility = View.GONE
                } else {
                    updateCard.visibility = View.VISIBLE
                }
            }

        adminRef.document("notice")
            .get().addOnSuccessListener { doc ->
                val show = doc.getBoolean("showNotice") as Boolean
                val notice = doc.getString("notice") as String
                if (show) {
                    noticeCard.visibility = View.VISIBLE
                    noticeText.text = notice
                } else {
                    noticeCard.visibility = View.GONE
                }
            }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun getTech() {
        techRef.orderBy("lastVisit", Query.Direction.DESCENDING).limit(6).get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val name = doc.get("name")
                    val imageUrl = doc.get("imageURL")
                    data.add(CategoryModel(imageUrl as String, name as String, doc.id))
                }
                adapter.notifyDataSetChanged()
            }.addOnFailureListener {
                {

                }
            }
    }


    fun fetchArticle() {
        articleRef.document("aJkx4UmUGkOY5WqI1Se3")
            .get().addOnSuccessListener { doc ->
                val title = doc.getString("title")
                val desc = doc.getString("description")
                val imageUrl = doc.getString("imageUrl")

                articleTitle.text = title
                Glide.with(this).load(imageUrl).into(articleImage)
            }
    }


    fun expertMode() {
        userRef.document(firebaseAuth.uid as String)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    document.data?.let {
                        expertRef.document(firebaseAuth.uid as String)
                            .set(it)
                    }
                }
            }
    }


    fun setToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { s ->
            val tokenhash = hashMapOf(
                "token" to s
            )
            tokenRef.child(firebaseAuth.uid.toString()).setValue(tokenhash)

            val hashMap = hashMapOf(
                "lastActive" to System.currentTimeMillis(),
                "token" to s,
                "versionName" to BuildConfig.VERSION_NAME,
                "versionCode" to BuildConfig.VERSION_CODE
            )
            userRef.document(firebaseAuth.uid.toString())
                .update(hashMap as Map<String, Any>)
                .addOnSuccessListener {
                }
                .addOnFailureListener { exc ->
                    run {
                    }
                }
        }

    }


    @SuppressLint("SetTextI18n")
    fun fetchUserDetail() {
        userRef.document(firebaseAuth.uid as String).get().addOnSuccessListener { documents ->
            if (documents.exists()) {
                val uImageUrl = documents.get("userImageUrl").toString()
                try {
                    Glide.with(this).load(uImageUrl).into(userImage2)
                } finally {

                }
            }
        }.addOnFailureListener { exception ->
            run {
                Toast.makeText(context, exception.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }



}