package uz.tima.xabardor.ui.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.tima.xabardor.R

class BottomSheetDialogPhotos(val url:String) : BottomSheetDialogFragment(){
    
    lateinit var oldview: View
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {
        
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout = bottomSheetDialog.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }
    
    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }
    
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        oldview  = inflater.inflate(R.layout.fragment_photo, container, false)
//        val binding : FragmentPhotoBinding= FragmentPhotoBinding.bind(oldview)
        val photoClose = oldview.findViewById<ImageView>(R.id.photo_close)
        val photoVieww = oldview.findViewById<PhotoView>(R.id.photo_vieww)

        photoClose.setOnClickListener {
            dismiss()
        }
//        binding.fragmentPhoto.setOnTouchListener(this)
        Glide.with(requireContext())
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(photoVieww)
        return oldview
    }
    
//    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//        return event?.action == MotionEvent.ACTION_DOWN
//    }
    
//    override fun onClick(v: View?) {
//
//    }


//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        oldview = inflater.inflate(layout, container, false)
//        oldview.setOnTouchListener { _, _ -> false }
//
//        return FrameLayout(requireContext()).apply {
//            setBackgroundColor(
//                Color.TRANSPARENT)
//            addView(oldview)
////        return inflater.inflate(layout,container,false)
//        }
//    }
}