package eninja.com.galua.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import eninja.com.galua.R;
import eninja.com.galua.fields.Field;

public class MainActivity extends Activity {

    EditText editFirstPolynomial;
    EditText editSecondPolynomial;

    Spinner spinOperation;

    TextView txtResultPolynomial;
    TextView txtModulusPolynomial;
    TextView txtResultBinary;
    TextView txtModulusBinary;

    /**
     * Создание Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        initComponents();
        clear(findViewById(R.id.btnClear)); // очистка экрана
    }

    /**
     * Инициализация графических компонентов
     */
    private void initComponents() {
        editFirstPolynomial = (EditText) findViewById(R.id.editFirstPolynomial);
        editSecondPolynomial = (EditText) findViewById(R.id.editSecondPolynomial);
        spinOperation = (Spinner) findViewById(R.id.spinOperation);
        txtResultPolynomial = (TextView) findViewById(R.id.txtResultPolynomial);
        txtModulusPolynomial = (TextView) findViewById(R.id.txtModulusPolynomial);
        txtResultBinary = (TextView) findViewById(R.id.txtResultBinary);
        txtModulusBinary = (TextView) findViewById(R.id.txtModulusBinary);
    }

    /**
     * Метод, вызываемый при нажатии на кнопку расчёта
     */
    public void calculate(View view) {
        Field a = new Field(editFirstPolynomial.getText().toString());
        Field b = new Field(editSecondPolynomial.getText().toString());

        int op = spinOperation.getSelectedItemPosition();
        // Деление
        if (op == 2) {
            Field[] resultArray = a.div(b);
            txtResultPolynomial.setText(resultArray[0].toPolynomialString());
            txtResultBinary.setText(resultArray[0].toBinaryString());
            txtModulusPolynomial.setText(resultArray[1].toPolynomialString());
            txtModulusBinary.setText(resultArray[1].toBinaryString());
            return;
        }

        Field result;
        // Сложение или умножение
        result = op == 1 ? a.mul(b) : a.add(b);
        txtResultPolynomial.setText(result.toPolynomialString());
        txtResultBinary.setText(result.toBinaryString());
        txtModulusPolynomial.setText("");
        txtModulusBinary.setText("");
    }

    /**
     * Метод, вызываемый при нажатии на кнопку очистки
     */
    public void clear(View view) {
        editFirstPolynomial.setText("");
        editSecondPolynomial.setText("");
        txtResultPolynomial.setText("");
        txtResultBinary.setText("");
        txtModulusPolynomial.setText("");
        txtModulusBinary.setText("");
    }
}
