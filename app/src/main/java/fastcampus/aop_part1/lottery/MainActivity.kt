package fastcampus.aop_part1.lottery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

//val : 초깃값이 할당되면 바꿀 수 없는 변수
//var : 나중에 값을 바꿀 수 있는 변수

class MainActivity : AppCompatActivity() {

    private val clearButton : Button by lazy { //by lazy : 변수 선언문 뒤에 사용
        findViewById<Button>(R.id.clearButton) // 소스에서 변수가 최초로 이용되는 순간 중괄호 자동 실행
    } // 그리고 그 결과값이 변수의 초깃값으로 할당된다. 여러줄일 경우에는 마지막 줄 실행 결과가 초깃값이 됨.

    private val addButton : Button by lazy {
        findViewById<Button>(R.id.addButton)
    }
    private val runButton : Button by lazy {
        findViewById<Button>(R.id.runButton)
    }

    private val numberPicker: NumberPicker by lazy {
        findViewById(R.id.numberPicker)
    }

    private val numberTextViewList : List<TextView> by lazy {
        listOf<TextView>(
            findViewById<TextView>(R.id.textView1),
            findViewById<TextView>(R.id.textView2),
            findViewById<TextView>(R.id.textView3),
            findViewById<TextView>(R.id.textView4),
            findViewById<TextView>(R.id.textView5),
            findViewById<TextView>(R.id.textView6)

        )
    }

    private var didRun = false

    private val pickNumberSet = hashSetOf<Int>() //자료구조 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker.minValue = 1 //피커의 최솟값=1
        numberPicker.maxValue = 45 //최댓값 = 45
0
        initRunButton()
        initAddButton()
        initClearButton()
    }

    private fun initAddButton() { //사용자가 고른 번호 추가
        addButton.setOnClickListener {
            if (didRun) { //이미 번호 6개가 선택된 경우 예외처리
                Toast.makeText(this,"초기화 후에 시도해 주시기 바랍니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pickNumberSet.size >= 5) { //번호가 5개 이상 선택된 경우 예외처리
                Toast.makeText(this,"번호는 5개까지만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pickNumberSet.contains(numberPicker.value)) { //이미 선택된 번호가 중복 선택될 수 없도록 예외처리
                Toast.makeText(this, "이미 선택된 번호입니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val textView = numberTextViewList[pickNumberSet.size]
            textView.isVisible = true
            textView.text = numberPicker.value.toString()
            setNumberBackground(numberPicker.value, textView)
            pickNumberSet.add(numberPicker.value)

        }
    }

    private fun setNumberBackground(number:Int, textView: TextView) {
        when (number) {
            in 1..10 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 11..20 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 21..30 -> textView.background = ContextCompat.getDrawable(this, R.drawable.cirecle_red)
            in 31..40 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_gray)
            else -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)

        }
}

    private fun initRunButton() {
        runButton.setOnClickListener {
            val list = getRandomNumber() //랜덤으로 추출된 6개의 번호를 저장한다.
            didRun = true //실행됨 상태를 true로 할당한다

            list.forEachIndexed { index, number -> //자료구조의 각 index, value에 대해 특정 작업을 수행할 수 있게 한다.
                //즉, [0]부터 순회함
                val textView = numberTextViewList[index]
                textView.text = number.toString()
                textView.isVisible = true
                setNumberBackground(number, textView)

            }

        }
    }

    private fun initClearButton() { //초기화 함수
        clearButton.setOnClickListener {
            pickNumberSet.clear() //자료구조 Set 초기화
            numberTextViewList.forEach { //값을 하나하나 꺼내주는 forEach문으로 모든 TextView를 보이지 않게 한다.
                it.isVisible = false
            }
            didRun = false //실행됨 불린 false로 초기화
        }
    }

    private fun getRandomNumber() : List<Int> {
        val numberList = mutableListOf<Int>() //list:읽기 전용, mutablelist: 일기/쓰기 가능
            .apply {
                for ( i in 1..45 ) {

                    if (pickNumberSet.contains(i)) //이미 추가되어 있는 번호는 제외한다.
                    {
                        continue
                    }
                    this.add(i)
                }
            }

        numberList.shuffle()
        val newList = pickNumberSet.toList() + numberList.subList(0,6 - pickNumberSet.size)
        //사용자가 선택한 번호가 저장되어 있는 리스트 + 랜덤으로 추출한 리스트를 합쳐 새로운 리스트를 만들고
        return newList.sorted() //반환한다.

    }
}