/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad Ean (Bogotá - Colombia)
 * Departamento de Tecnologías de la Información y Comunicaciones
 * Licenciado bajo el esquema Academic Free License version 2.1
 * <p>
 * Proyecto Evaluador de Expresiones Postfijas
 * Fecha: Febrero 2021
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package universidadean.desarrollosw.postfijo;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.*;

/**
 * Esta clase representa una clase que evalúa expresiones en notación polaca o
 * postfija. Por ejemplo: 4 5 +
 */
public class EvaluadorPostfijo {

    /**
     * Permite saber si la expresión en la lista está balanceada
     * o no. Cada elemento de la lista es un elemento. DEBE OBlIGATORIAMENTE
     * USARSE EL ALGORITMO QUE ESTÁ EN EL ENUNCIADO.
     */
    static boolean estaBalanceada(List<String> expresion) {
        Stack<String> delimitadores = new Stack<>();

        for (String elemento : expresion) {
            if (elemento.equals("(") || elemento.equals("{") || elemento.equals("[")) {
                delimitadores.push(elemento);
            } else if (elemento.equals(")") || elemento.equals("}") || elemento.equals("]")) {
                if (delimitadores.isEmpty()) {
                    return false; // La pila está vacía, la expresión no está balanceada
                } else {
                    String apertura = delimitadores.pop();
                    if (!((apertura.equals("(") && elemento.equals(")")) ||
                            (apertura.equals("{") && elemento.equals("}")) ||
                            (apertura.equals("[") && elemento.equals("]")))) {
                        return false; // Los delimitadores no coinciden, la expresión no está balanceada
                    }
                }
            }
        }

        return delimitadores.isEmpty();
    }

    /**
     * Transforma la expresión, cambiando los símbolos de agrupación
     * de corchetes ([]) y llaves ({}) por paréntesis ()
     */
    static void reemplazarDelimitadores(List<String> expresion) {
        for (int i = 0; i < expresion.size(); i++) {
            String elemento = expresion.get(i);
            if (elemento.equals("[")) {
                expresion.set(i, "(");
            } else if (elemento.equals("{")) {
                expresion.set(i, "(");
            } else if (elemento.equals("]")) {
                expresion.set(i, ")");
            } else if (elemento.equals("}")) {
                expresion.set(i, ")");
            }
        }
    }

    /**
     * Realiza la conversión de la notación infija a postfija
     *
     * @return la expresión convertida a postfija
     * OJO: Debe usarse el algoritmo que está en el enunciado OBLIGATORIAMENTE
     */
    static List<String> convertirAPostfijo(List<String> expresion) {
        Stack<String> pila = new Stack<>();
        List<String> salida = new ArrayList<>();

        for (String elemento : expresion) {
            if (esOperando(elemento)) {
                salida.add(elemento);
            } else if (elemento.equals("(")) {
                pila.push(elemento);
            } else if (elemento.equals(")")) {
                while (!pila.isEmpty() && !pila.peek().equals("(")) {
                    salida.add(pila.pop());
                }
                pila.pop(); // Quitar el paréntesis izquierdo
            } else { // Es un operador
                while (!pila.isEmpty() && precedencia(pila.peek()) >= precedencia(elemento)) {
                    salida.add(pila.pop());
                }
                pila.push(elemento);
            }
        }

        while (!pila.isEmpty()) {
            salida.add(pila.pop());
        }

        return salida;
    }

    // Funciones auxiliares para verificar operadores y precedencia
    static boolean esOperador(String elemento) {
        return elemento.equals("+") || elemento.equals("-") || elemento.equals("*") || elemento.equals("/");
    }

    static boolean esOperando(String elemento) {
        return !esOperador(elemento) && !elemento.equals("(") && !elemento.equals(")");
    }

    static int precedencia(String operador) {
        switch (operador) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            default:
                return 0;
        }
    }

    /**
     * Realiza la evaluación de la expresión postfijo utilizando una pila
     *
     * @param expresion una lista de elementos con números u operadores
     * @return el resultado de la evaluación de la expresión.
     */
    static int evaluarPostFija(List<String> expresion) {
        Stack<Integer> pila = new Stack<>();

        for (String elemento : expresion) {
            if (esNumero(elemento)) {
                pila.push(Integer.parseInt(elemento));
            } else if (esOperador(elemento)) {
                int operand2 = pila.pop();
                int operand1 = pila.pop();
                int resultado = realizarOperacion(operand1, operand2, elemento);
                pila.push(resultado);
            }
        }

        return pila.pop();
    }

    // Funciones auxiliares para verificar números y operadores
    static boolean esNumero(String elemento) {
        try {
            Integer.parseInt(elemento);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    static int realizarOperacion(int operand1, int operand2, String operador) {
        switch (operador) {
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "*":
                return operand1 * operand2;
            case "/":
                if (operand2 != 0) {
                    return operand1 / operand2;
                } else {
                    throw new ArithmeticException("División por cero");
                }
            default:
                throw new IllegalArgumentException("Operador no válido: " + operador);
        }
    }
}