package com.adrpien.lunarlogictask.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentResultListener
import com.adrpien.lunarlogictask.R
import com.adrpien.lunarlogictask.alertdialog.NumberPickerDialog
import com.adrpien.lunarlogictask.databinding.FragmentMainBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class MainFragment : Fragment() {

    private lateinit var tempNumberByteArray: ByteArray

    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    // Input values
    private var value1: Int = 0
    private var value2: Int = 0
    private var value3: Int = 0


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

        // value1Button onclick implementation
        binding.value1Button.setOnClickListener {
            // Open SignatureDialog when signatureImageButtonClicked
            val dialog = NumberPickerDialog()
            // Dialog fragment result listener
            // Saves ByteArray stored in bundle and converts to Bitmap; sets this bitmap as numberImageButton image
            requireActivity().supportFragmentManager.setFragmentResultListener(
                getString(R.string.number_request_key),
                viewLifecycleOwner,
                FragmentResultListener { requestKey, result ->
                    val bitmap = fetchBitmapFromBundle(result)
                    recognizeHandwriting(bitmap, value1, binding.value1Button, binding.value1EditText)
                })
            // show MyTimePicker
            dialog.show(childFragmentManager, getString(R.string.number_dialog_tag))
        }

        // value1Button onclick implementation
        binding.value2Button.setOnClickListener {
            // Open SignatureDialog when signatureImageButtonClicked
            val dialog = NumberPickerDialog()
            // Dialog fragment result listener
            // Saves ByteArray stored in bundle and converts to Bitmap; sets this bitmap as numberImageButton image
            requireActivity().supportFragmentManager.setFragmentResultListener(
                getString(R.string.number_request_key),
                viewLifecycleOwner,
                FragmentResultListener { requestKey, result ->
                    val bitmap = fetchBitmapFromBundle(result)
                    recognizeHandwriting(bitmap, value2, binding.value2Button, binding.value2EditText)
                })
            // show MyTimePicker
            dialog.show(childFragmentManager, getString(R.string.number_dialog_tag))        }

        // value3Button onclick implementation
        binding.value3Button.setOnClickListener {
            // Open SignatureDialog when signatureImageButtonClicked
            val dialog = NumberPickerDialog()
            // Dialog fragment result listener
            // Saves ByteArray stored in bundle and converts to Bitmap; sets this bitmap as numberImageButton image
            requireActivity().supportFragmentManager.setFragmentResultListener(
                getString(R.string.number_request_key),
                viewLifecycleOwner,
                FragmentResultListener { requestKey, result ->
                    val bitmap = fetchBitmapFromBundle(result)
                    recognizeHandwriting(bitmap, value3, binding.value3Button, binding.value3EditText)
                })
            // show MyTimePicker
            dialog.show(childFragmentManager, getString(R.string.number_dialog_tag))
        }

        // Adjust button text when editText text changed
        binding.value1EditText.doAfterTextChanged {
            binding.value1Button.text = binding.value1EditText.text
            value1 = binding.value1EditText.text.toString().toInt()
        }

        // Adjust button text when editText text changed
        binding.value2EditText.doAfterTextChanged {
            binding.value2Button.text = binding.value2EditText.text
            value2 = binding.value2EditText.text.toString().toInt()
        }

        // Adjust button text when editText text changed
        binding.value3EditText.doAfterTextChanged {
            binding.value3Button.text = binding.value3EditText.text
            value3 = binding.value3EditText.text.toString().toInt()
        }

        // actionButton onClick implementation
        binding.actionButton.setOnClickListener {
            if(binding.value1EditText.text.toString() != "" && binding.value2EditText.text.toString() != "" && binding.value3EditText.text.toString() != "") {
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

    // Fetches bitmap using bundle from alert dialog
    private fun fetchBitmapFromBundle(bundle: Bundle): Bitmap {
        tempNumberByteArray = bundle.getByteArray("number")!!
        val options = BitmapFactory.Options()
        options.inMutable = true
        val bmp = BitmapFactory.decodeByteArray(
            tempNumberByteArray,
            0,
            tempNumberByteArray.size,
            options
        )
        return bmp
    }

    // Recognizes handwriting using delivered bitmap
    private fun recognizeHandwriting(bitmap: Bitmap, variableToFill: Int, buttonToFill: Button, editTextToFill: EditText): Int {
        var outputValue = 999
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image = InputImage.fromBitmap(bitmap, 0)
        val result = recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val outputString = visionText.text
                val result = outputString.filter { it.isDigit() }
                outputValue = if (result.isNotEmpty()) { result.toInt() } else { 997 }
                buttonToFill.text = outputValue.toString()
                editTextToFill.setText(outputValue.toString())
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "UPS! Something wrong happend!", Toast.LENGTH_SHORT).show()
                outputValue = 998
                buttonToFill.text = outputValue.toString()
                editTextToFill.setText(outputValue.toString())
            }
        return outputValue
    }

    // Show result
    private fun showResult() {
        binding.resultTextView.text =
            "Results: \nvalue1 = ${outputList[0]}, \nvelue2 = ${outputList[1]}, \nvalue3 = ${outputList[2]}"
    }

    // Fetches numbers from editexts
    private fun getInputList(): MutableList<Int> {
        return mutableListOf(
            value1,
            value2,
            value3
        )

    }

    /* Example:
    When all three numbers of input list are divisible by three, number of used steps is zero,
    so we can use remaining six steps to to make result even bigger.
    */
    private fun useRemainingSteps(list: MutableList<Int>): MutableList<Int> {

        val listOfOutputLists = mutableListOf<MutableList<Int>>()
        val listOfSums = mutableListOf<Int>()

        val listOfOutputLists2 = mutableListOf<MutableList<Int>>()
        val listOfSums2 = mutableListOf<Int>()

        var tempOutputList = list.toMutableList()
        var tempNumberOfSteps = numberOfSteps

        // If all 3 numbers are divisible by 3
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

        // If number of left steps is equall 3 or more
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