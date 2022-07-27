package ge.gmegrelishvili.messengerapp.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ge.gmegrelishvili.messengerapp.R
import ge.gmegrelishvili.messengerapp.view.main.MainActivityPagerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var pagerAdapter: ViewPager2
    private lateinit var navigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pagerAdapter = findViewById(R.id.main_activity_view_pager)
        pagerAdapter.adapter = MainActivityPagerAdapter(this)

        navigationView = findViewById(R.id.main_bottom_navigation_view)
        navigationView.setOnItemSelectedListener { item ->
            pagerAdapter.currentItem = when (item.itemId) {
                R.id.main_home_page -> HomePage
                R.id.main_profile_page -> ProfilePage
                else -> HomePage
            }
            true
        }

        findViewById<FloatingActionButton>(R.id.search_user_fab).setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }

    companion object {
        const val HomePage = 0
        const val ProfilePage = 1
    }

}