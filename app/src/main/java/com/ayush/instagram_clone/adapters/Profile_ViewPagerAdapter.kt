package com.ayush.instagram_clone.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class Profile_ViewPagerAdapter(fm:FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val fragment_list = mutableListOf<Fragment>()
    val titles_list = mutableListOf<String>()
    override fun getCount(): Int {
        return fragment_list.size
    }

    override fun getItem(position: Int): Fragment {
        return fragment_list.get(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles_list.get(position)
    }

    fun addFragments(fragment: Fragment,title:String){
        fragment_list.add(fragment)
        titles_list.add(title)
    }


}