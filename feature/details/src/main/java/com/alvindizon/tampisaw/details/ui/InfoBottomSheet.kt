package com.alvindizon.tampisaw.details.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvindizon.tampisaw.details.R
import com.alvindizon.tampisaw.details.databinding.DialogPhotoDetailsBinding
import com.alvindizon.tampisaw.details.viewmodel.DetailsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoBottomSheet(private val tags: List<String?>?, private val listener: (String) -> Unit) :
    BottomSheetDialogFragment() {

    private val viewModel: DetailsViewModel by activityViewModels()

    private lateinit var adapter: TagAdapter

    private var binding: DialogPhotoDetailsBinding? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState)

        bottomSheetDialog.setOnShowListener {
            val bottomSheet = bottomSheetDialog
                .findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet).apply {
                skipCollapsed = true
                state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_photo_details, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Add a click listener for each list item
        adapter = TagAdapter { listener.invoke(it) }

        binding?.apply {
            viewModel = this@InfoBottomSheet.viewModel
            lifecycleOwner = this@InfoBottomSheet
            tagList.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            tagList.adapter = adapter
            adapter.submitList(tags)
            executePendingBindings()
        }
    }

    companion object {
        val TAG = InfoBottomSheet::class.java.simpleName
        fun newInstance(tags: List<String?>?, listener: (String) -> Unit) =
            InfoBottomSheet(tags, listener)
    }
}
