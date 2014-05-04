/*
 Instituto Tecnológico de Costa Rica.
 IC-4700 Lenguajes de Programación.
 II Proyecto Programado.
 Integrantes:
    José Andrés García Sáenz <jags9415@gmail.com>.
    Leonardo Madrigal Valverde <lmadrigal09@gmail.com>.
 I Semestre 2014.
*/

package com.distgraph.util;

/**
 * Clase que provee ciertas funciones útiles para el sistema.
 */
public class Utilities 
{
    /**
     * Realiza un split del String que le ingresa y convierte cada valor a double.
     * Ejemplo: splitAndCast("0:1:0.4", ":") => {0.0, 1.0, 0.4}
     * 
     * @param s El String que se desea parsear.
     * @param delimit El delimitador usado para realizar el split.
     * @return Un arreglo de double con cada valor del resultado del split.
     * 
     * @throws NumberFormatException Si el String no se puede parsear.
     */
    public static double[] splitAndCast(String s, String delimit) throws NumberFormatException
    {
        String[] temp = s.split(delimit);
        double[] result = new double[temp.length];
        
        for (int i=0; i<temp.length; i++)
        {
            result[i] = Double.parseDouble(temp[i]);
        }
        
        return result;
    }
}