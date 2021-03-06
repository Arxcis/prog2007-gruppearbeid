package com.example.gruppearbeid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.gruppearbeid.databinding.FragmentAddImageBinding
import com.example.gruppearbeid.types.myViewModel
import com.example.gruppearbeid.util.Constants
import com.example.gruppearbeid.util.ItemViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [addImage.newInstance] factory method to
 * create an instance of this fragment.
 */
class addImage : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var fragmentXML: FragmentAddImageBinding
    private val viewModel: ItemViewModel by activityViewModels()

    private lateinit var searchTerm: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        viewModel.selectedItem.observe(this, Observer {
            searchTerm = it
            Log.d("addImageFrag", it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentXML = FragmentAddImageBinding.inflate(inflater, container, false)
        fragmentXML.btnAddImageFragment.setOnClickListener {
            val intent = Intent(activity, ChooseImage::class.java).apply {
                putExtra(Constants.API_DATA_ENTITY, searchTerm)
            }
            startActivity(intent)
        }

        return fragmentXML.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment addImage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            addImage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}