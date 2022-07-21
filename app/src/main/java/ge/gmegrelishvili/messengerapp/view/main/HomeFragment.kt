package ge.gmegrelishvili.messengerapp.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import ge.gmegrelishvili.messengerapp.R

class HomeFragment(fragmentActivity: FragmentActivity) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val createdView = inflater.inflate(R.layout.fragment_home, container, false)
        return createdView
    }

}