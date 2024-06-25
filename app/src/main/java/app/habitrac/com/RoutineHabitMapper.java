package app.habitrac.com;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class RoutineHabitMapper implements HabitMapper {
    private static final String[] AlIMEN_MESSAGES = {
            "Beber un vaso de agua al despertar",
            "Comer una pieza de fruta fresca",
            "Sustituir una bebida azucarada por agua",
            "Comer una ensalada pequeña con la comida principa",
            "Reducir la cantidad de azúcar en el café o té",
            "Incluir una porción de vegetales en una comida",
            "Preparar un snack saludable (por ejemplo, nueces)"
    };

    private static final String[] ESTUDIO_MESSAGES = {
            "Leer un artículo o capítulo de un libro",
            "Ver un video educativo",
            "Tomar notas durante una lectura",
            "Revisar apuntes de una clase",
            "Hacer una lista de temas de estudio",
            "Organizar el espacio de estudio",
            "Establecer un horario de estudio diario"
    };

    private static final String[] EJERCICIO_MESSAGES = {
            "Hacer una caminata de 15 minutos",
            "Realizar 10 minutos de estiramientos",
            "Subir y bajar escaleras durante 5 minutos",
            "Hacer 10 minutos de yoga",
            "Realizar una sesión corta de baile",
            "Hacer 5 minutos de saltos de tijera",
            "Practicar una rutina de respiración profunda y estiramientos"
    };
    private static final String[] MEDI_MESSAGES = {
            "Practicar respiración profunda durante 5 minutos",
            "Hacer una meditación guiada corta (5-10 minutos)",
            "Realizar una sesión de atención plena (mindfulness) de 5 minuto",
            "Escuchar música relajante durante 10 minutos",
            "Tomarse 5 minutos para concentrarse en la respiración",
            "Meditar en la naturaleza durante 5 minutos",
            "Hacer una visualización positiva durante 5 minutos"
    };

    private static final String[] ORG_MESSAGES = {
            "Hacer la cama al levantarse",
            "Limpiar el escritorio",
            "Organizar el correo electrónico",
            "Hacer una lista de tareas para el día",
            "Ordenar un área pequeña (por ejemplo, un cajón)",
            "Despejar la mesa de trabajo",
            "Guardar los objetos en su lugar después de usarlos"
    };

    private static final String[] LEC_MESSAGES = {
            "Leer un artículo corto",
            "Leer una página de un libro",
            "Leer un poema",
            "Leer una noticia",
            "Leer un párrafo de una novela",
            "Leer la introducción de un libro",
            "Leer una cita inspiradora"
    };

    private static final String[] SUE_MESSAGES = {
            "Evitar cafeína después del mediodía",
            "Apagar las pantallas 30 minutos antes de dormir",
            "Practicar respiración profunda antes de dormir",
            "Mantener una rutina de dormir constante",
            "Hacer una lista de tareas para el día siguiente antes de dormir",
            "Leer un libro relajante antes de acostarse",
            "Escuchar música suave para relajarse"
    };

    private Random random = new Random();
    private Timer timer = new Timer();
    private SharedPreferences sharedPreferences,sharedPreferences1;

    @Override
    public String getHabitMessage(String itemName) {
        switch (itemName) {
            case "Buena Alimentación":
                String lastEstudioMessage1 = getLastEstudioMessage1();
                if (lastEstudioMessage1 == null || isTimeToUpdateEstudioMessage1()) {
                    // Update the message if it's time or no previous message exists
                    String newMessage1 = AlIMEN_MESSAGES[random.nextInt(AlIMEN_MESSAGES.length)];
                    updateEstudioMessage1(newMessage1);
                    return newMessage1;
                } else {
                    // Return the last updated message
                    return lastEstudioMessage1;
                }
            case "Estudio":
                // Retrieve the last updated message
                String lastEstudioMessage = getLastEstudioMessage();
                if (lastEstudioMessage == null || isTimeToUpdateEstudioMessage()) {
                    // Update the message if it's time or no previous message exists
                    String newMessage = ESTUDIO_MESSAGES[random.nextInt(ESTUDIO_MESSAGES.length)];
                    updateEstudioMessage(newMessage);
                    return newMessage;
                } else {
                    // Return the last updated message
                    return lastEstudioMessage;
                }
            case "Ejercicio":
                String lastEstudioMessage3 = getLastEstudioMessage3();
                if (lastEstudioMessage3 == null || isTimeToUpdateEstudioMessage3()) {
                    // Update the message if it's time or no previous message exists
                    String newMessage3 = EJERCICIO_MESSAGES[random.nextInt(EJERCICIO_MESSAGES.length)];
                    updateEstudioMessage3(newMessage3);
                    return newMessage3;
                } else {
                    // Return the last updated message
                    return lastEstudioMessage3;
                }
            case "Meditación":
                String lastEstudioMessage4 = getLastEstudioMessage4();
                if (lastEstudioMessage4 == null || isTimeToUpdateEstudioMessage4()) {
                    // Update the message if it's time or no previous message exists
                    String newMessage4 = MEDI_MESSAGES[random.nextInt(MEDI_MESSAGES.length)];
                    updateEstudioMessage4(newMessage4);
                    return newMessage4;
                } else {
                    // Return the last updated message
                    return lastEstudioMessage4;
                }
            case "Organización":
                String lastEstudioMessage5 = getLastEstudioMessage5();
                if (lastEstudioMessage5 == null || isTimeToUpdateEstudioMessage5()) {
                    // Update the message if it's time or no previous message exists
                    String newMessage5 = ORG_MESSAGES[random.nextInt(ORG_MESSAGES.length)];
                    updateEstudioMessage5(newMessage5);
                    return newMessage5;
                } else {
                    // Return the last updated message
                    return lastEstudioMessage5;
                }
            case "Lectura":
                String lastEstudioMessage6 = getLastEstudioMessage6();
                if (lastEstudioMessage6 == null || isTimeToUpdateEstudioMessage6()) {
                    // Update the message if it's time or no previous message exists
                    String newMessage6 = LEC_MESSAGES[random.nextInt(LEC_MESSAGES.length)];
                    updateEstudioMessage6(newMessage6);
                    return newMessage6;
                } else {
                    // Return the last updated message
                    return lastEstudioMessage6;
                }
            case "Sueño":
                String lastEstudioMessage7 = getLastEstudioMessage7();
                if (lastEstudioMessage7 == null || isTimeToUpdateEstudioMessage7()) {
                    // Update the message if it's time or no previous message exists
                    String newMessage7 = SUE_MESSAGES[random.nextInt(SUE_MESSAGES.length)];
                    updateEstudioMessage7(newMessage7);
                    return newMessage7;
                } else {
                    // Return the last updated message
                    return lastEstudioMessage7;
                }
            // Agregue más mapeos para otros elementos
            default:
                return itemName;
        }
    }

    private String getLastEstudioMessage() {
        return sharedPreferences.getString("estudio_message", null);
    }
    private String getLastEstudioMessage1() {
        return sharedPreferences.getString("estudio_message1", null);
    }
    private String getLastEstudioMessage3() {
        return sharedPreferences.getString("estudio_message3", null);
    }
    private String getLastEstudioMessage4() {
        return sharedPreferences.getString("estudio_message4", null);
    }
    private String getLastEstudioMessage5() {
        return sharedPreferences.getString("estudio_message5", null);
    }
    private String getLastEstudioMessage6() {
        return sharedPreferences.getString("estudio_message6", null);
    }
    private String getLastEstudioMessage7() {
        return sharedPreferences.getString("estudio_message7", null);
    }

    private boolean isTimeToUpdateEstudioMessage() {
        long lastUpdateTime = sharedPreferences.getLong("estudio_last_update", 0);
        long currentTime = System.currentTimeMillis();

        // Check if 12 hours have passed since the last update
        return (currentTime - lastUpdateTime) >=  3 * 60 * 1000; // 12 hours in milliseconds
    }
    private boolean isTimeToUpdateEstudioMessage3() {
        long lastUpdateTime = sharedPreferences.getLong("estudio_last_update3", 0);
        long currentTime = System.currentTimeMillis();

        // Check if 12 hours have passed since the last update
        return (currentTime - lastUpdateTime) >=  3 * 60 * 1000; // 12 hours in milliseconds
    }
    private boolean isTimeToUpdateEstudioMessage4() {
        long lastUpdateTime = sharedPreferences.getLong("estudio_last_update4", 0);
        long currentTime = System.currentTimeMillis();

        // Check if 12 hours have passed since the last update
        return (currentTime - lastUpdateTime) >=  3 * 60 * 1000; // 12 hours in milliseconds
    }
    private boolean isTimeToUpdateEstudioMessage5() {
        long lastUpdateTime = sharedPreferences.getLong("estudio_last_update5", 0);
        long currentTime = System.currentTimeMillis();

        // Check if 12 hours have passed since the last update
        return (currentTime - lastUpdateTime) >=  3 * 60 * 1000; // 12 hours in milliseconds
    }
    private boolean isTimeToUpdateEstudioMessage6() {
        long lastUpdateTime = sharedPreferences.getLong("estudio_last_update6", 0);
        long currentTime = System.currentTimeMillis();

        // Check if 12 hours have passed since the last update
        return (currentTime - lastUpdateTime) >=  3 * 60 * 1000; // 12 hours in milliseconds
    }
    private boolean isTimeToUpdateEstudioMessage7() {
        long lastUpdateTime = sharedPreferences.getLong("estudio_last_update7", 0);
        long currentTime = System.currentTimeMillis();

        // Check if 12 hours have passed since the last update
        return (currentTime - lastUpdateTime) >=  3 * 60 * 1000; // 12 hours in milliseconds
    }
    private boolean isTimeToUpdateEstudioMessage1() {
        long lastUpdateTime = sharedPreferences.getLong("estudio_last_update1", 0);
        long currentTime = System.currentTimeMillis();

        // Check if 12 hours have passed since the last update
        return (currentTime - lastUpdateTime) >= 3 * 60 * 1000; // 12 hours in milliseconds
    }

    private void updateEstudioMessage(String newMessage) {
        sharedPreferences.edit()
                .putString("estudio_message", newMessage)
                .putLong("estudio_last_update", System.currentTimeMillis())
                .apply();
    }
    private void updateEstudioMessage1(String newMessage1) {
        sharedPreferences.edit()
                .putString("estudio_message1", newMessage1)
                .putLong("estudio_last_update1", System.currentTimeMillis())
                .apply();
    }
    private void updateEstudioMessage3(String newMessage3) {
        sharedPreferences.edit()
                .putString("estudio_message3", newMessage3)
                .putLong("estudio_last_update3", System.currentTimeMillis())
                .apply();
    }
    private void updateEstudioMessage4(String newMessage4) {
        sharedPreferences.edit()
                .putString("estudio_message4", newMessage4)
                .putLong("estudio_last_update1", System.currentTimeMillis())
                .apply();
    }
    private void updateEstudioMessage5(String newMessage5) {
        sharedPreferences.edit()
                .putString("estudio_message5", newMessage5)
                .putLong("estudio_last_update5", System.currentTimeMillis())
                .apply();
    }
    private void updateEstudioMessage6(String newMessage6) {
        sharedPreferences.edit()
                .putString("estudio_message6", newMessage6)
                .putLong("estudio_last_update6", System.currentTimeMillis())
                .apply();
    }
    private void updateEstudioMessage7(String newMessage7) {
        sharedPreferences.edit()
                .putString("estudio_message7", newMessage7)
                .putLong("estudio_last_update7", System.currentTimeMillis())
                .apply();
    }

    public RoutineHabitMapper(Context context) { // Pass context to the constructor
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        sharedPreferences = context.getSharedPreferences("habit_messages", Context.MODE_PRIVATE);
    }
}