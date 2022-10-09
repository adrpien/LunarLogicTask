package com.adrpien.lunarlogictask.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.adrpien.lunarlogictask.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var inputList: MutableList<Int>
    private lateinit var outputList: MutableList<Int>
    private var numberOfSteps = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // actionButton onClick implementation
        binding.actionButton.setOnClickListener {

            if(binding.firstValueEditText.text.toString() != "" && binding.secondValueEditText.text.toString() != "" && binding.thirdValueEditText.text.toString() != "") {

                // Capture input values
                inputList = mutableListOf(
                    binding.firstValueEditText.text.toString().toInt(),
                    binding.secondValueEditText.text.toString().toInt(),
                    binding.thirdValueEditText.text.toString().toInt(),
                )

                // Get result
                outputList = getResult(inputList)

                // Show result implementation
                binding.resultTextView.text =
                    "Results: \nvalue1 = ${outputList[0]}, \nvelue2 = ${outputList[1]}, \nvalue3 = ${outputList[2]}"
            } else {
                Toast.makeText(context, "Fill empty fields!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getResult(list: MutableList<Int>): MutableList<Int> {
        numberOfSteps = 6
        outputList = mutableListOf<Int>()
        list.forEach {  item ->
            if(!isDivisibleByThree(item)){
                outputList.add(makeDivisibleByThree(item))
            } else {
                outputList.add(item)
            }
        }

        // Use remaining steps if numberOfSteps bigger or equal 3
        useRemainingSteps()

        return outputList
    }

    private fun makeDivisibleByThree(number: Int): Int {
        val outputNumber: Int = number
        var tempList = splitNumberIntoDigits(number)
        if(outputNumber % 3 == 2) {
            tempList[0] += 1
            numberOfSteps -= 1
        } else {
            tempList[0] += 2
            numberOfSteps -= 2
        }
        return mergeDigitsIntoNumber(tempList)

    }

    private fun useRemainingSteps() {
        while (numberOfSteps >= 3 ){
            var tempDigitList = splitNumberIntoDigits(outputList.max())
            tempDigitList[0] += 3
            val tempNumber = mergeDigitsIntoNumber(tempDigitList)
            outputList[outputList.indexOf(outputList.max())] = tempNumber
            numberOfSteps -= 3
        }
    }

    private fun mergeDigitsIntoNumber(list: MutableList<Int>): Int {
        list.reverse()
        var x = 1
        var outputNumber: Int = 0
         for(item in list){
             outputNumber = outputNumber + (item * x)
             x *= 10
         }

        return outputNumber
    }

    // Checks if sum of all digits is divisible by 3
    private fun isDivisibleByThree(number: Int): Boolean{
        var sumOfDigits: Int = 0
        val list: MutableList<Int> = splitNumberIntoDigits(number)
        for (item in list){
            sumOfDigits += item
        }
        return sumOfDigits % 3 == 0
    }

    // Returns list with each digit of value in reversed order
    private fun splitNumberIntoDigits(number: Int): MutableList<Int> {
        var tempValue = number
        val outputDigitList = mutableListOf<Int>()
        while (tempValue > 0 ) {
            val digit: Int = tempValue % 10
            outputDigitList.add(digit)
            tempValue /= 10
        }
        outputDigitList.reverse()
        return outputDigitList
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}