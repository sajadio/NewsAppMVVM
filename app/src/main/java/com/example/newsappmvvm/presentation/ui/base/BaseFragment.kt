package com.example.newsappmvvm.presentation.ui.base

import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.newsappmvvm.R
import com.example.newsappmvvm.presentation.ui.NewsActivity
import com.example.newsappmvvm.presentation.ui.viewmodel.NewsViewModel

abstract class BaseFragment<DB : ViewDataBinding>(@LayoutRes private val layoutId: Int) :
    Fragment() {

    abstract val visibilityIconToolbar: List<Int>
    // shared viewModel
    lateinit var viewModel: NewsViewModel
    private var _binding: DB? = null
    val binding: DB get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner
        viewModel = (activity as NewsActivity).viewModel
        setHasOptionsMenu(true)
        initial()
    }

    abstract fun initial()


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        val search = menu.findItem(R.id.searching)
        val favorite = menu.findItem(R.id.favoriteFragment)
        val deleteAll = menu.findItem(R.id.deleteAllItem)

        visibilityIconToolbar.forEach {
            when (it) {
                search.itemId -> search.isVisible = false
                favorite.itemId -> favorite.isVisible = false
                deleteAll.itemId -> deleteAll.isVisible = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // for prevent memory leaks
        _binding = null
    }
}
