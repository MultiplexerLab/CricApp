package com.multiplexer.cricapp.fragments.fixture

import RecyclerViewItemAnimation
import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.devhoony.lottieproegressdialog.LottieProgressDialog
import com.irozon.sneaker.Sneaker
import com.multiplexer.cricapp.adapters.FixtureAdapter
import com.multiplexer.cricapp.adapters.LiveFixtureAdapter
import com.multiplexer.cricapp.databinding.FragmentCricketBinding
import com.multiplexer.cricapp.utlis.ProgressBar
import com.multiplexer.cricapp.viewmodels.CricketViewModel
import java.util.*


@Suppress("DEPRECATION", "NAME_SHADOWING")
class CricketFragment : Fragment() {
    private val args: CricketFragmentArgs by navArgs()
    private lateinit var viewModel: CricketViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var liveRecyclerView: RecyclerView
    private lateinit var progressDialog: LottieProgressDialog
    private var _binding: FragmentCricketBinding? = null
    private val binding get() = _binding!!
    private lateinit var layoutManager: LinearLayoutManager
    private val mHandler = Handler()
    private lateinit var mRunnable: Runnable
    private var dateDiff: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCricketBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[CricketViewModel::class.java]
        recyclerView = binding.photosGrid
        liveRecyclerView = binding.liveRecycleView
        recyclerView.setHasFixedSize(true)
        recyclerView.isDrawingCacheEnabled = true
        recyclerView.setItemViewCacheSize(900)
        recyclerView.layoutManager = LinearLayoutManager(context)

        liveRecyclerView.setHasFixedSize(true)
        liveRecyclerView.isDrawingCacheEnabled = true
        liveRecyclerView.setItemViewCacheSize(900)
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(liveRecyclerView)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        liveRecyclerView.layoutManager = layoutManager
        liveRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1) {
                    recyclerView.scrollToPosition(0)
                }
            }
        })

        liveRecyclerView.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    view.parent.requestDisallowInterceptTouchEvent(true)
                    mHandler.removeCallbacks(mRunnable) // Pause auto-scrolling
                }
                MotionEvent.ACTION_UP -> {
                    view.parent.requestDisallowInterceptTouchEvent(false)
                    mHandler.postDelayed(mRunnable, 20) // Resume auto-scrolling after a delay
                }
            }
            false
        }

        progressDialog = ProgressBar.ProgressBarSingleton.showProgressBar(
            requireContext(), "Loading Matches..."
        )

        when (args.date) {
            getDate(-3) -> {
                liveRecyclerView.visibility = View.GONE
                getMatches(-3, args.date)
            }
            getDate(-2) -> {
                liveRecyclerView.visibility = View.GONE
                getMatches(-2, args.date)
            }
            getDate(-1) -> {
                liveRecyclerView.visibility = View.GONE
                getMatches(-1, args.date)
            }
            getDate(0) -> {
                getMatches(0, args.date)
                viewModel.getLiveFixtures()
                viewModel.cachedLiveFixtures.observe(viewLifecycleOwner) { liveFixtures ->
                    ProgressBar.ProgressBarSingleton.hideProgressBar(progressDialog)
                    liveRecyclerView.adapter =
                        LiveFixtureAdapter(requireContext(), viewModel, liveFixtures)
                }
            }
            getDate(1) -> {

                liveRecyclerView.visibility = View.GONE
                getMatches(1, args.date)
            }
            getDate(2) -> {
                liveRecyclerView.visibility = View.GONE
                getMatches(2, args.date)
            }
            else -> {
                liveRecyclerView.visibility = View.GONE
                getMatches(3, args.date)
            }
        }
        val handler = Handler()
        handler.postDelayed({
            ProgressBar.ProgressBarSingleton.hideProgressBar(progressDialog)
        }, 5000)

        binding.swipeToRefresh.setOnRefreshListener {
            binding.swipeToRefresh.isRefreshing = false
            viewModel.getFixtures(dateDiff)
            viewModel.getLiveFixtures()

        }
        startAutoScroll()
    }

    private fun getMatches(dateVariation: Int, date: String) {
        viewModel.getFixtures(dateVariation)
        viewModel.cachedFixtures.observe(viewLifecycleOwner) {
            ProgressBar.ProgressBarSingleton.hideProgressBar(progressDialog)
            recyclerView.adapter = FixtureAdapter(
                requireContext(), viewModel, it, date
            )
            recyclerView.itemAnimator = RecyclerViewItemAnimation()
            Sneaker.with(requireActivity()).setTitle("Success!!")
                .setMessage("Updated successfully!!!").sneakSuccess()
        }
        dateDiff = dateVariation
    }

    private fun startAutoScroll() {
        mRunnable = Runnable {
            liveRecyclerView.smoothScrollBy(20, 0) // Change the value 10 to adjust the scroll speed
            mHandler.postDelayed(
                mRunnable, 10
            )
        }
        mHandler.postDelayed(mRunnable, 20)
    }

    private fun getDate(diff: Int): String {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val calendar = Calendar.getInstance()
        calendar.time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(currentDate)
        calendar.add(Calendar.DATE, diff)
        val nextDayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        return if (diff == 0) {
            "${currentDate}T00:00:00.000000Z,${currentDate}T23:59:00.000000Z"
        } else {
            "${nextDayDate}T00:00:00.000000Z,${nextDayDate}T23:59:00.000000Z"
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onStop() {
        super.onStop()
        stopAutoScroll()
        mHandler.removeCallbacks(mRunnable)
        liveRecyclerView.setOnTouchListener(null)

    }

    private fun stopAutoScroll() {
        mHandler.removeCallbacks(mRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopAutoScroll()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        stopAutoScroll()
        startAutoScroll()
    }

    override fun onPause() {
        super.onPause()
        stopAutoScroll()
    }
}