import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.kuit4_android_retrofit.R
import com.example.kuit4_android_retrofit.databinding.FragmentFavoriteBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class Pbs : AppCompatActivity() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var behavior: BottomSheetBehavior<FrameLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // BottomSheetBehavior 초기화
        val bottomSheet = findViewById<FrameLayout>(R.id.bottomSheet)
        behavior = BottomSheetBehavior.from(bottomSheet)

        // Bottom Sheet 초기 상태 설정
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED

        // 상태 리스너 등록
        persistentBottomSheetEvent()

        // 버튼 이벤트 처리
        binding.showBottomSheetButton.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED // Bottom Sheet 확장
        }

        binding.hideBottomSheetButton.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED // Bottom Sheet 축소
        }
    }

    private fun persistentBottomSheetEvent() {
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d(TAG, "onSlide: 드래그 중")
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> Log.d(TAG, "onStateChanged: 접음")
                    BottomSheetBehavior.STATE_DRAGGING -> Log.d(TAG, "onStateChanged: 드래그 중")
                    BottomSheetBehavior.STATE_EXPANDED -> Log.d(TAG, "onStateChanged: 펼침")
                    BottomSheetBehavior.STATE_HIDDEN -> Log.d(TAG, "onStateChanged: 숨김")
                    BottomSheetBehavior.STATE_SETTLING -> Log.d(TAG, "onStateChanged: 고정됨")
                }
            }
        })
    }
}
