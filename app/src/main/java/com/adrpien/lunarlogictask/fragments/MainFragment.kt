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

    /*
       ************** Lifecycle functions **************
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // actionButton onClick implementation
        binding.actionButton.setOnClickListener {
            if(binding.firstValueEditText.text.toString() != "" && binding.secondValueEditText.text.toString() != "" && binding.thirdValueEditText.text.toString() != "") {
                numberOfSteps = 6
                inputList = getInputList()
                outputList = getResult(inputList)
                showResult()
            } else {
                Toast.makeText(context, "Fill empty fields!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /*
        ************** My functions *****************
     */

    // Show result
    private fun showResult() {
        binding.resultTextView.text =
            "Results: \nvalue1 = ${outputList[0]}, \nvelue2 = ${outputList[1]}, \nvalue3 = ${outputList[2]}"
    }

    // Fetches numbers from editexts
    private fun getInputList(): MutableList<Int> {
        return mutableListOf(
            binding.firstValueEditText.text.toString().toInt(),
            binding.secondValueEditText.text.toString().toInt(),
            binding.thirdValueEditText.text.toString().toInt(),
        )

    }

    /* Example:
    When all three numbers of input list are divisible by three, number of used steps is zero,
    so we can use remaining six steps to to make result even bigger.
    We can do it by adding 3 to highest digit to the biggest number of list (the most valuable digit),
    to keep number divisible by three.
    */
    private fun useRemainingSteps(list: MutableList<Int>): MutableList<Int> {

        val listOfOutputLists = mutableListOf<MutableList<Int>>()
        val listOfSums = mutableListOf<Int>()

        val listOfOutputLists2 = mutableListOf<MutableList<Int>>()
        val listOfSums2 = mutableListOf<Int>()

        var tempOutputList = list.toMutableList()
        var tempNumberOfSteps = numberOfSteps

        if (tempNumberOfSteps == 6) {
            for ((index, item) in tempOutputList.withIndex()) {
                val tempList = tempOutputList.toMutableList()
                var tempDigitList = splitNumberIntoDigits(tempOutputList[index])
                var x = 0
                while (x < 3) {
                    tempDigitList = addOne(tempDigitList)
                    x += 1

                }
                val tempNumber = mergeDigitsIntoNumber(tempDigitList)
                tempList[index] = tempNumber
                listOfOutputLists2.add(tempList)
                listOfSums2.add(tempList.sum())
            }
            tempOutputList = listOfOutputLists2[listOfSums2.indexOf(listOfSums2.max())]
            tempNumberOfSteps -= 3

        }
        if (tempNumberOfSteps >= 3) {
            for ((index, item) in tempOutputList.withIndex()){
                val tempList = tempOutputList.toMutableList()
                var tempDigitList = splitNumberIntoDigits(tempOutputList[index])
                var x = 0
                while (x < 3) {
                    tempDigitList = addOne(tempDigitList)
                    x += 1
                }
                val tempNumber = mergeDigitsIntoNumber(tempDigitList)
                tempList[index] = tempNumber
                listOfOutputLists.add(tempList)
                listOfSums.add(tempList.sum())
            }
            tempNumberOfSteps -= 3
            tempOutputList = listOfOutputLists[listOfSums.indexOf(listOfSums.max())]

        }
        return tempOutputList
    }

    // Adds one safely (does not add one to digit 9)
    private fun addOne(listOfDigits: MutableList<Int>): MutableList<Int> {
        val outputList = listOfDigits.toMutableList()
        var leftToAdd = 1
        for ((index, item) in outputList.withIndex()) {
            if (leftToAdd > 0 && outputList[index] != 9) {
                outputList[index] += 1
                leftToAdd -= 1
            }
        }
        return outputList
    }

    // Returns result
    private fun getResult(list: MutableList<Int>): MutableList<Int> {
        var outputList = getListDivisibleByThree(list)
        outputList = useRemainingSteps(outputList)
        return outputList
    }

    // Returns list of values divisible by three
    private fun getListDivisibleByThree(list: MutableList<Int>): MutableList<Int> {
        val outputList = mutableListOf<Int>()
        list.forEach { item ->
            if (!isDivisibleByThree(item)) {
                outputList.add(makeNumberDivisibleByThree(item))
            } else {
                outputList.add(item)
            }
        }
        return outputList
    }

    // Changes indivisible by three number into number divisible by three
    private fun makeNumberDivisibleByThree(number: Int): Int {
        var tempList = splitNumberIntoDigits(number)
        if(number % 3 == 2) {
            tempList = addOne(tempList)
            numberOfSteps -= 1
        } else {
            var x = 1
            for(x in 1 ..2){
                tempList = addOne(tempList)
                numberOfSteps -= 1
            }
        }
        return mergeDigitsIntoNumber(tempList)
    }

    // Merges all digits in list into number
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

    // Returns list with each digit of input number in reversed order
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
}