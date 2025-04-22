package gob.issste.gys.service;

import gob.issste.gys.JdbcTemplateDemo01Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParamsValidatorService {

    private final String [] ADSC_CENTRALES = {"00", "09", "50", "60", "63"};
    Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);
    private final String[] SQL_INJECTIONS = new String[] {
            ";", "--", "'", "/*", "*/", "@@", "@", "NULL", "TRUE", "FALSE", "NOT", "AND", "OR",
            "IS", "BETWEEN", "LIKE", "IN", "EXISTS", "ALL", "ANY", "SOME", "WITHIN", "INTERSECT",
            "COLLATE", "AS", "ON", "USING", "JOIN", "CROSS", "INNER", "OUTER", "LEFT", "RIGHT",
            "NATURAL", "WHERE", "ORDER BY", "GROUP BY", "HAVING", "LIMIT", "OFFSET", "SELECT",
            "INSERT", "UPDATE", "DELETE", "DROP", "EXEC", "EXECUTE", "ALTER", "CREATE", "SHUTDOWN",
            "GRANT", "REVOKE", "UNION", "LOAD_FILE", "INTO OUTFILE", "INTO DUMPFILE"
    };

    private static final String[] CSV_INJECTIONS = {
            "+", "-", "@", "#", "|", "%", "^", "&", "*", "[", "]", "{", "}", ";", ":", "'",
            "\"", "\\", "?", "!", "~", "<", ">", "\t", "\r", "\n",
            "SUMA(", "PROMEDIO(", "SI(", "BUSCAR(", "BUSCARV(", "BUSCARH(", "INDICE(", "COINCIDIR(", "CONCATENAR(",
            "SUBTOTALES(", "DESREF(", "HOY(", "AHORA(", "FECHA(", "TEXTO(", "VALOR(", "CELDA(", "INFO(", "HIPERVINCULO(",
            "suma(", "promedio(", "si(", "buscar(", "buscarv(", "buscarh(", "indice(", "coincidir(", "concatenar(", "subtotales(",
            "desref(", "hoy(", "ahora(", "fecha(", "texto(", "valor(", "celda(", "info(", "hipervinculo("
    };
    public boolean sqlInjectionObjectValidator(List<String> inputData) {
        List<String> list = new ArrayList<>();
        for (String inputDataElement : inputData) {
            if(inputDataElement != null)
                for (String sqlInjection : SQL_INJECTIONS) {
                    if (inputDataElement.toUpperCase().contains(sqlInjection)) {
                        list.add(inputDataElement.toUpperCase());
                    }
                }
        }
        return list.toArray().length != 0;
    }

    public boolean adscValidator(String param){
        for (String value : ADSC_CENTRALES){
            if(value.contains(param)){
                return true;
            }
        }
        return false;
    }

    //    public boolean sqlInjectionObjectValidator(List<String> inputData) {
//        List<String> list = new ArrayList<>();
//        for (String inputDataElement : inputData) {
//            if (inputDataElement != null) {
//                String[] words = inputDataElement.toUpperCase().split("\\s+");
//                for (String word : words) {
//                    for (String sqlInjection : SQL_INJECTIONS) {
//                        if (word.equals(sqlInjection)) {
//                            list.add(inputDataElement.toUpperCase());
//                        }
//                    }
//                }
//            }
//        }
//        return !list.isEmpty();
//    }

    public static boolean csvInjectionObjectValidator(List<String> inputData) {
        List<String> foundEvillist = new ArrayList<>();
        for (String inputDataElement : inputData) {
            if(inputDataElement != null)
                for (String sqlInjection : CSV_INJECTIONS) {
                    if (inputDataElement.toUpperCase().contains(sqlInjection)) {
                        foundEvillist.add(inputDataElement.toUpperCase());
                    }
                }
        }
        return foundEvillist.toArray().length != 0;
    }


    public static boolean csvInjectionValidator(String inputData) {
        if (inputData == null || inputData.isEmpty()) {
            return false;
        }
        for(String injection : CSV_INJECTIONS) {
            if(inputData.contains(injection)){
                return true;
            }
        }
        return false;
    }

    public boolean regexValidation(String regex, String value){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public boolean validate(List<String> regexList, List<String> paramList) {
        if (regexList.size() != paramList.size()) {
            throw new IllegalArgumentException("Las listas deben tener el mismo tamaño.");
        }

        for (int i = 0; i < regexList.size(); i++) {
            String regex = regexList.get(i);
            String param = paramList.get(i);
            if (!Pattern.matches(regex, param)) {
                return false;
            }
        }
        return true;
    }
}
