package com.example.dawam.ui.waqf_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.example.dawam.R
import com.example.dawam.api.ApiManager
import com.example.dawam.api.model.waqfResponse.WaqfResponse
import com.example.dawam.databinding.ActivityWaqfDetailsBinding
import com.example.dawam.ui.Constants.WAQF_IMAGE_EXTRA
import com.example.dawam.ui.waqf_details.recycler_view.LineItem
import com.example.dawam.ui.waqf_details.recycler_view.WaqfDetailsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WaqfDetailsActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityWaqfDetailsBinding



    lateinit var adapter: WaqfDetailsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewBinding= ActivityWaqfDetailsBinding.inflate(layoutInflater)
//        setContentView(viewBinding.root)
        viewBinding= DataBindingUtil.setContentView(this,R.layout.activity_waqf_details)

        //intent
        val id = intent.getIntExtra("ID",1)

       loadWaqfDetails(id)
        initListeners()


    }
     lateinit var items:List<LineItem>
    private fun loadWaqfDetails(id :Int){
        ApiManager.getApis().getWaqfById(id).enqueue(object : Callback<WaqfResponse>{
            override fun onResponse(call: Call<WaqfResponse>, response: Response<WaqfResponse>) {
                if(response.isSuccessful){
                    val waqf = response.body()
                     items = listOf(
                        LineItem("اسم الوقف:", waqf!!.waqfName),
                        LineItem("اسم الواقف:", waqf.founderName),
                        LineItem("تاريخ الوقف هجريًا:",waqf.establishmentDateH),
                        LineItem("تاريخ الوقف ميلاديًا:", waqf.establishmentDate),
                        LineItem("نوع الوقف:", waqf.waqfType),
                        LineItem("تصنيف الوقف:", waqf.waqfActivity),
                        LineItem("ريع الوقف:",waqf.waqfCity+ " "+ waqf.waqfCountry),
                        LineItem("وصف الوقف:", waqf.waqfDescription)
                    )

                }
            }

            override fun onFailure(call: Call<WaqfResponse>, t: Throwable) {
                viewBinding.content.loadingIndicator.isVisible=false
                showErrorLayout(t.localizedMessage as String, id)
            }

        })
        initRecyclerView(items )

    }
    private fun showLoadingLayout() {
        viewBinding.content.errorLayout.isVisible=false
        viewBinding.content.loadingIndicator.isVisible=true
    }

    private fun showErrorLayout(errorMessage:String, id:Int) {
        viewBinding.content.errorLayout.isVisible=true
        viewBinding.content.loadingIndicator.isVisible=false
        viewBinding.content.errorMessage.text= errorMessage
        viewBinding.content.tryAgainBtn.setOnClickListener{
            loadWaqfDetails(id)
        }
    }

    private fun initRecyclerView(items: List<LineItem>) {
        adapter= WaqfDetailsAdapter(items)

         viewBinding.content.recyclerView.adapter=adapter
    }





    private fun initListeners() {
        with(viewBinding) {
            backBtn.setOnClickListener{
                finish()
            }
        }
    }
}