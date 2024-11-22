package com.example.kuit4_android_retrofit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.kuit4_android_retrofit.adapter.RVPopularMenuAdapter
import com.example.kuit4_android_retrofit.data.CategoryData
import com.example.kuit4_android_retrofit.data.MenuData
import com.example.kuit4_android_retrofit.databinding.FragmentHomeBinding
import com.example.kuit4_android_retrofit.databinding.ItemCategoryBinding
import com.example.kuit4_android_retrofit.retrofit.RetrofitObject
import com.example.kuit4_android_retrofit.retrofit.service.CategoryService
import com.example.kuit4_android_retrofit.retrofit.service.PopularService
import retrofit2.Call
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var rvPopularMenuAdapter: RVPopularMenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        fetchCategoryInfo()

        initMenuRV()

        return binding.root
    }

    private fun initMenuRV() {
        val service = RetrofitObject.retrofit.create(PopularService::class.java)
        val call =service.getCategories()

        call.enqueue(
            object :retrofit2.Callback<List<MenuData>>{
                override fun onResponse(
                    call: Call<List<MenuData>>,
                    response: Response<List<MenuData>>
                ) {
                    if(response.isSuccessful){
                        val menuDataResponse = response.body()
                        // 데이터가 성공적으로 받아와졌을 때
                        if (!menuDataResponse.isNullOrEmpty()) {
                            //데이터 넣기
                            rvPopularMenuAdapter=RVPopularMenuAdapter(requireContext(),menuDataResponse)
                            binding.rvMainPopularMenus.adapter =rvPopularMenuAdapter
                            binding.rvMainPopularMenus.layoutManager=
                                LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                        } else {
                            Log.d("menuNull", "menuNull")// 빈값을 받아온 경우
                        }
                    }else{
                        Log.d("menuNoResponse", "menuNoResponse")// 서버 응답이 실패했을 때 (상태코드 5**)
                    }
                }
                override fun onFailure(call: Call<List<MenuData>>, t: Throwable) {
                    Log.d("menuCritical", "menuCritical")
                }

            }
        )


    }

    private fun fetchCategoryInfo() {
        val service = RetrofitObject.retrofit.create(CategoryService::class.java)
        val call =service.getCategories()

        call.enqueue(
            object : retrofit2.Callback<List<CategoryData>>{
                override fun onResponse(
                    call: Call<List<CategoryData>>,
                    response: Response<List<CategoryData>>
                ) {
                    if(response.isSuccessful) {
                        val categoryResponse = response.body()
                        // 데이터가 성공적으로 받아와졌을 때
                        if (!categoryResponse.isNullOrEmpty()) {
                            showCategoryInfo(categoryResponse)
                        } else {
                            Log.d("categoryNull", "categoryNull")// 빈값을 받아온 경우
                        }
                    }else{
                        Log.d("categoryNoResponse", "categoryNoResponse")// 서버 응답이 실패했을 때 (상태코드 5**)
                    }
                }

                // 네트워크 오류
                override fun onFailure(call: Call<List<CategoryData>>, t: Throwable) {
                    Log.d("categoryCritical", "categoryCritical")
                }

            }
        )
    }
    private fun showCategoryInfo(categoryList: List<CategoryData>) {
        // 레이아웃 인플레이터를 사용해 카테고리 항목을 동적으로 추가
        val inflater = LayoutInflater.from(requireContext())
        binding.llMainMenuCategory.removeAllViews() // 기존 항목 제거

        categoryList.forEach { category ->// 동적으로 한개씩 넣기
            val categoryBinding = ItemCategoryBinding.inflate(inflater, binding.llMainMenuCategory, false)

            // 이미지 로딩: Glide 사용 (이미지 URL을 ImageView에 로드)
            Glide
                .with(this)
                .load(category.categoryImg)
                .into(categoryBinding.sivCategoryImg)

            // 카테고리 이름 설정
            categoryBinding.tvCategoryName.text = category.categoryName


            // 레이아웃에 카테고리 항목 추가
            binding.llMainMenuCategory.addView(categoryBinding.root)
        }
    }
}
