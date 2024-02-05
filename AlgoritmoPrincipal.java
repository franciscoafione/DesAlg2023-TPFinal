package TPFinal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Francisco Afione
 */
public class AlgoritmoPrincipal {

    public static void menu(Alumno[][] matrizAlumnos, Alumno[] arrEgresados, Alumno[] arrIngresantes, int[] arrRepitentes) {
        //Muestra las opciones de carga o modificacion de datos en el sistema.
        Scanner sc = new Scanner(System.in);
        int opcion, grado;
        do {
            System.out.println("1 - Pasar de grado. \n"
                    + "2 - Calcular el promedio general de cada curso. \n"
                    + "3 - Listar alumnos de un curso. \n"
                    + "4 - Listar los alumnos graduados. \n"
                    + "5 - Contar la cantidad de vacantes en toda la escuela. \n"
                    + "6 - Agregar un alumno nuevo. \n"
                    + "7 - Salir.");
            opcion = sc.nextInt();

            if (opcion < 0 || opcion > 7) {
                System.out.println("Opcion no valida, intentelo nuevamente.");
            } else {
                switch (opcion) {
                    case 1:
                        //Pasar de grado
                        
                        //Egresados
                        egresar(matrizAlumnos, arrEgresados, arrRepitentes);
                        //Pasar de grado
                        pasajeNormalDeGrado(matrizAlumnos, arrIngresantes, arrRepitentes);
                        //Agregar ingresantes:
                        agregarIngresantes(matrizAlumnos, arrIngresantes);

                        System.out.println("Accion exitosa.");
                        break;
                    case 2:
                        //Promedio general
                        mostrarPromediosGenerales(matrizAlumnos);
                        break;
                    case 3: 
                        //Lista de alumnos de un grado
                        System.out.println("Inserte el grado que desea listar");
                        grado = sc.nextInt();
                        if (grado < 1 || grado > 7){
                            System.out.println("Ese grado no existe. Solo hay de primero a septimo. Intentelo de nuevo.");
                        } else {
                            ordenAlfabeticoSeleccion(matrizAlumnos, grado -1);
                            listarAlumnos(matrizAlumnos, grado - 1);
                        }
                        break;
                    case 4:
                        //Lista los alumnos egresados, ordenadamente
                        System.out.println("Alumnos egresados:");
                        listarEgresados(arrEgresados);
                        break;
                    case 5:
                        //Vacantes disponibles
                        int cant;
                        cant = contarVacantes(matrizAlumnos, 0, 0);
                        System.out.println("La escuela tiene: " + cant + " vacantes disponibles.");
                        break;
                    case 6:
                        //Nuevo alumno
                        agregarNuevoAlumno(matrizAlumnos, arrIngresantes, arrRepitentes);
                        break;
                    case 7:
                        System.out.println("Fin.");
                        break;
                    default:
                }
            }
        } while (opcion != 7);

    }

    //Modulos de carga inicial
    public static void cargaIngresantes(Alumno[] arr) {
        //carga el arreglo de alumnos ingresantes a partir del archivo.

        BufferedReader br = null;
        String linea;

        try {
            br = new BufferedReader(new FileReader("C:\\Users\\Francisco Afione\\Documents\\NetBeansProjects\\DesAlg2023\\src\\main\\java\\TPFinal\\ListaIngresantes.txt"));
            int i = 0;

            // Crear objetos a partir de archivo.
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");

                Alumno alumno = new Alumno(datos[0], datos[1], Integer.parseInt(datos[2]), Integer.parseInt(datos[3]), Double.parseDouble(datos[4]));

                // Encontrar el primer elemento vacio.
                i = primerNullArreglo(arr);

                // Asignar el alumno a esa posición.
                if (i < arr.length) {
                    arr[i] = alumno;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void cargaAlumnos(Alumno[][] matriz) {
        //Carga la matriz de Alumnos filas = grados, columnas = alumnos.
        int columna, grado;
        String linea;
        BufferedReader br;
        br = null;
        columna = 0;

        try {
            br = new BufferedReader(new FileReader("C:\\Users\\Francisco Afione\\Documents\\NetBeansProjects\\DesAlg2023\\src\\main\\java\\TPFinal\\ListaAlumnos.txt"));

            // Crear objetos a partir de archivo.
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");

                Alumno alumno = new Alumno(datos[0], datos[1], Integer.parseInt(datos[2]), Integer.parseInt(datos[3]), Double.parseDouble(datos[4]));
                grado = alumno.getGrado();

                // Encontrar la primera columna vacía para el grado correspondiente
                while (columna < matriz[0].length && matriz[grado - 1][columna] != null) {
                    columna++;
                }

                // Asignar el alumno a esa posición
                if (columna < matriz[0].length) {
                    matriz[grado - 1][columna] = alumno;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void cargaLegajosRepitentes(int[] legajosRepitentes) {
        //Carga un arreglo de enteros con los legajos de aquellos alumnos que repiten de grado.
        BufferedReader br = null;
        String linea;
        int legajo;
        int i = 0;

        try {
            br = new BufferedReader(new FileReader("C:\\Users\\Francisco Afione\\Documents\\NetBeansProjects\\DesAlg2023\\src\\main\\java\\TPFinal\\ListaDesaprobados.txt"));

            // carga los legajos al arreglo.
            while ((linea = br.readLine()) != null) {
                legajo = Integer.parseInt(linea);

                while (i < legajosRepitentes.length && legajosRepitentes[i] != 0) {
                    i++;
                }

                if (i < legajosRepitentes.length) {
                    legajosRepitentes[i] = legajo;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Modulos utiles
    public static int buscaLegajo(int[] arrLegajos, int legajo) {
        //Busca un numero de legajo en un arreglo de enteros. Metodo de busqueda binaria.
        int medio, ini, fin, pos;
        boolean encontrado;

        ini = 1;
        fin = arrLegajos.length;
        encontrado = false;
        pos = -1;

        while (!encontrado && ini < fin) {
            medio = (ini + fin) / 2;
            if (legajo < arrLegajos[medio]) {
                encontrado = true;
                pos = medio;
            } else {
                ini = medio + 1;
            }
        }

        return pos;
    }
    
    public static int legajoMaximo(Alumno[][] matriz, Alumno[] arrIngresantes, int[] arrRepitentes) {
        int legajoMaximo, i, j;
        legajoMaximo = 0;

        // ... En la matriz prinipal.
        for (i = 0; i < matriz.length; i++) {
            for (j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j] != null && matriz[i][j].getLegajo() > legajoMaximo) {
                    legajoMaximo = matriz[i][j].getLegajo();
                }
            }
        }

        // ... En el arreglo de repitentes.
        for (i = 0; i < arrRepitentes.length; i++) {
            if (arrRepitentes[i] > legajoMaximo) {
                legajoMaximo = arrRepitentes[i];
            }
        }

        // ... En el arreglo de ingresantes
        for (i = 0; i < arrIngresantes.length; i++) {
            if (arrIngresantes[i] != null && arrIngresantes[i].getLegajo() > legajoMaximo) {
                legajoMaximo = arrIngresantes[i].getLegajo();
            }
        }

        return legajoMaximo;
    }

    public static void ordenarFila(Alumno[][] m, int fila) {
        //Si en una fila de la matriz hay posiciones vacias, mueve todos los alumnos a las primeras posiciones, dejando las vacias al final.
        int i, j;
        Alumno alumno;
        for (i = 0; i < m[0].length - 1; i++) {
            for (j = 0; j < m[0].length - i - 1; j++) {
                if (m[fila][j] == null && m[fila][j + 1] != null) {
                    alumno = m[fila][j + 1];
                    m[fila][j] = alumno;
                    m[fila][j + 1] = null;
                }
            }
        }
    }

    public static void ordenarArreglo(Alumno[] arr) {
        // Ordena un arreglo de Alumno del mismo modo que 'ordenarFila'.
        int i, j;
        Alumno alumno;
        for (i = 0; i < arr.length - 1; i++) {
            for (j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] == null && arr[j + 1] != null) {
                    alumno = arr[j + 1];
                    arr[j] = alumno;
                    arr[j + 1] = null;
                }
            }
        }
    }

    public static int primerNullMatriz(Alumno[][] m, int grado) {
        //Retorna la primer posicion vacia en una fila de la matriz. En caso de estar lleno, devuelve m.length.
        //grado -1
        int col;
        col = 0;

        while (col < m[0].length && m[grado][col] != null) {
            col++;
        }
        return col;
    }

    public static int primerNullArreglo(Alumno[] arr) {
        //Retorna la primer posicion vacia en un arreglo. En caso de estar lleno, devuelve arr.length.
        int col;
        col = 0;
        while (col < arr.length && arr[col] != null) {
            col++;
        }
        return col;
    }
    
    public static double calculaPromedio(Alumno[][] matriz, int grado, int col, int cantElementos) {
        //Realiza la suma de los promedios particulares de tods los alumnos en un grado.
        double suma;

        if (col == matriz[0].length || matriz[grado][col] == null) {
            suma = 0;
        } else {
            suma = matriz[grado][col].getPromedio()/(double)cantElementos;
            suma += calculaPromedio(matriz, grado, col + 1, cantElementos);
        }
        
        return suma;
    }
        
    public static int cantElementosFila(Alumno[][] matriz, int grado){
        // Calcula la cantidad de alumnos que tiene un grado.
        int cantElementos;
        cantElementos = 0;
        
        while (cantElementos < matriz[0].length && matriz[grado][cantElementos] != null) {
            cantElementos++;
        }
        
        return cantElementos;
    }
    
    public static void ordenAlfabeticoSeleccion(Alumno[][] matriz, int grado) {
        // Ordena los alumnos de un grado alfabéticamente segun sus apellidos y nombres.
        int i, j, iMin;
        Alumno alumno;
        String jApellido, iMinApellido, jNombre, iMinNombre;

        for (i = 0; i < matriz[grado].length - 1; i++) {
            iMin = i;

            //Recorre la fila en busca del primer alumno en el alfabeto (iMin).
            for (j = i + 1; j < matriz[grado].length; j++) {
                if (matriz[grado][j] != null && matriz[grado][iMin] != null){
                    jApellido = matriz[grado][j].getApellido();
                    iMinApellido = matriz[grado][iMin].getApellido();
                    jNombre = matriz[grado][j].getNombre();
                    iMinNombre = matriz[grado][iMin].getNombre();
                
                    if (matriz[grado][j] != null && (jApellido.compareTo(iMinApellido) < 0 || (jApellido.equals(iMinApellido) && jNombre.compareTo(iMinNombre) < 0))) {
                        iMin = j;
                    }
                }
                
                
            }
            
            // Intercambio los elementos.
            if (iMin != i) {
                alumno = matriz[grado][i];
                matriz[grado][i] = matriz[grado][iMin];
                matriz[grado][iMin] = alumno;
            }
        }
    }
    
    public static void ordenaPorPromedio(Alumno[] arr) {
        // Ordena un arreglo de Alumno de manera descendiente segun sus promedios. ordenamiento burbuja
        int n = arr.length;
        Alumno temp;

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] != null && arr[j + 1] != null && arr[j].getPromedio() < arr[j + 1].getPromedio()) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    //Modulos, funcionalidades del sistema
    public static void agregarIngresantes(Alumno[][] m, Alumno[] arrIngresantes) {
        // Agrega alumnos ingresantes a todos los grados que tengan espacio. Salvo por el primer grado, Solo ingresan 3 por grado.
        int i, col, grado;
        int[] cantIngresantes = {0, 0, 0, 0, 0, 0, 0}; //Cuantifica la cantidad de ingresantes por curso (max: 3).
        i = 0;

        while (i < arrIngresantes.length && arrIngresantes[i] != null) {
            grado = arrIngresantes[i].getGrado() - 1;
            //Si el ingresante no esta en 8vo, es decir, que ya se graduo en otra escuela.
            if (grado < 7){
                // Encontrar la primera columna vacía para el grado correspondiente
                col = primerNullMatriz(m, grado);

                // Asignar el alumno a esa posición
                if (col < m[0].length && (cantIngresantes[grado] < 3 || grado == 0)) {
                    m[grado][col] = arrIngresantes[i];
                    arrIngresantes[i] = null;
                    cantIngresantes[grado]++;
                }
                i++;
            } else {
                arrIngresantes[i] = null;
            }
            
        }

        ordenarArreglo(arrIngresantes);
    }

    public static void egresar(Alumno[][] matriz, Alumno[] arrEgresados, int[] arrRepitentes) {
        // Mueve a los alumnos del septimo grado que aprueban al arreglo de Egresados
        int col, colAux, grado;
        Alumno alumno;
        col = 0;
        grado = 6;

        while (col < matriz[0].length && matriz[grado][col] != null) {
            if (buscaLegajo(arrRepitentes, matriz[grado][col].getLegajo()) == -1) {
                alumno = matriz[grado][col];
                matriz[grado][col] = null;
                alumno.pasarDeGrado();

                // Primer posicion vacia en el arreglo de egresados
                colAux = primerNullArreglo(arrEgresados);

                // Asigno el alumno a esa posición
                if (colAux < arrEgresados.length) {
                    arrEgresados[colAux] = alumno;
                }
            }
            col++;
        }

        //Ordenar el septimo grado
        ordenarFila(matriz, 6);
    }
    
    public static void repitenDeGrado (Alumno [][] matriz, Alumno[] desaprobados, int[] legajosRepitentes, int cantDesaprobados){
        //agrega a los alumnos de un arreglo de alumnos desaprobados a sus respectivos grados en la matriz principal, luego los elimina del primer arreglo.
        int i, grado, colAux;
        
        for (i = 0; i < cantDesaprobados; i++) {
            grado = desaprobados[i].getGrado() - 1;
            colAux = primerNullMatriz(matriz, grado);
            if (colAux < matriz[0].length) {
                matriz[grado][colAux] = desaprobados[i];
                desaprobados[i] = null;
            }
        }
        ordenarArreglo(desaprobados);
        
    }
    
    public static void pasajeDeGradoFueraDelSistema(Alumno[] arrIngresantes, int[] arrRepitentes){
        // Recorre la lista de ingresantes para que pasen de grado por fuera del sistema.
        int col;
        col = 0;
        while (col < arrIngresantes.length && arrIngresantes[col] != null) {
            if (buscaLegajo(arrRepitentes, arrIngresantes[col].getLegajo()) == -1) {
                if (arrIngresantes[col].getGrado() <= 7) {
                    arrIngresantes[col].pasarDeGrado();
                } else {
                    arrIngresantes[col] = null;
                }
            }
            col++;
        }
    }

    public static void pasajeNormalDeGrado(Alumno[][] matriz, Alumno[] arrIngresantes, int[] arrRepitentes) {
        // Realiza el pasaje de grado de los alumnos de todos los niveles menos el septimo.
        int grado, col, colAux, cantDesaprobados;
        Alumno alumno;
        col = 0;
        cantDesaprobados = 0;
        Alumno[] desaprobados = new Alumno[matriz.length * matriz[0].length]; // Arreglo para almacenar desaprobados

        // Recorrido y pasaje de grado
         for (grado = matriz.length - 2; grado >= 0; grado--) {
            while (col < matriz[0].length && matriz[grado][col] != null) {
                if (buscaLegajo(arrRepitentes, matriz[grado][col].getLegajo()) == -1) {
                    matriz[grado][col].pasarDeGrado();
                    alumno = matriz[grado][col];
                    colAux = primerNullMatriz(matriz, grado + 1);

                    if (colAux < matriz[0].length) {
                        matriz[grado + 1][colAux] = alumno;
                    }
                } else {
                    // Si es repitente, lo agrega al arreglo de desaprobados y lo elimina de la matriz principal.
                    desaprobados[cantDesaprobados++] = matriz[grado][col];
                    // Quito al repitente del arreglo de legajos.
                    int posLegajo;
                    posLegajo = buscaLegajo(arrRepitentes, matriz[grado][col].getLegajo());
                    arrRepitentes[posLegajo] = 0;
                    // Lo borro temporalmente de la matriz principal.
                    matriz[grado][col] = null;
                }
                col++;
            }
            // Ordeno la fila.
            ordenarFila(matriz, grado);
        }

        // Si hay espacio, agrega a los alumnos desaprobados a sus respectivos grados.
        repitenDeGrado(matriz, desaprobados, arrRepitentes, cantDesaprobados);

        // Pasaje de grado para ingresantes (fuera del sistema).
        pasajeDeGradoFueraDelSistema(arrIngresantes, arrRepitentes);
        
    }
    
    public static void mostrarPromediosGenerales(Alumno[][] matriz){
        //Lista todos los cursos con sus promedios generales.
        int i;
        
        for(i =0; i< matriz.length; i++){
            System.out.println((i+1) + "° curso" + " - Promedio: " + calculaPromedio(matriz, i, 0, cantElementosFila(matriz, i)));
        }
    }
    
    public static void listarAlumnos(Alumno[][] m, int grado) {
        //grado-1
        int i, fin;
        fin = primerNullMatriz(m, grado);

        for (i = 0; i < fin; i++) {
            System.out.println(m[grado][i].toString());
        }
    }
    
    public static void listarEgresados(Alumno[] arr){
        //Muestra los datos de todos los alumnos egresados, ordenados por sus promedios, de forma descendiente.
        int i;
        i = 0;
        ordenaPorPromedio(arr);
        
        while (i < arr.length && arr[i] != null){
            System.out.println(arr[i].toString());
            i++;
        }
    }
    
    public static int contarVacantes(Alumno[][] m, int i, int j){
        //Cuenta la cantidad de vacantes disponibles en la escuela.
        int cant;
        cant = 0;
        
        if(i == m.length){
            cant = 0;
        } else {
            if (j == m[0].length){
                cant = contarVacantes(m, i+1, 0);
            } else {
                if (m[i][j] == null){
                    cant = 1;
                }
                cant += contarVacantes(m, i, j+1);
            }
        }
        
        return cant;
    }
    
    public static void agregarNuevoAlumno(Alumno[][] m, Alumno[] arrIngresantes, int[] arrRepitentes){
        //Agrega un nuevo alumno creado por el usuario.
        Scanner sc = new Scanner(System.in);
        Alumno nuevoAlumno;
        String apellido, nombre;
        int legajo, grado;
        double prom;
        int posicionVacia;
        
        System.out.println("Ingresar nuevo alumno:");
        System.out.println("Apellido?");
        apellido = sc.next();
        System.out.println("Nombre?");
        nombre = sc.next();
        System.out.println("A que grado ira?");
        grado = sc.nextInt();
        System.out.println("Promedio? (Si es inicial, ingrese 0");
        prom = sc.nextDouble();
        
        legajo = legajoMaximo(m, arrIngresantes, arrRepitentes) + 1;
        
        nuevoAlumno = new Alumno(apellido, nombre, legajo, grado-1, prom);
        //Busca la primer posicion vacia en ese grado.
        posicionVacia = primerNullMatriz(m, grado-1);
        
        if(posicionVacia != -1){
            m[grado-1][posicionVacia] = nuevoAlumno;
            System.out.println("Ya hemos cargado al nuevo alumno en el sistema.");
        } else {
            System.out.println("Lo sentimos, el grado al que quiere ingresar al alumno esta lleno.");
        }
    }

    public static void main(String[] args) {
        Alumno[][] matrizAlumnos = new Alumno[7][30];
        Alumno[] arregloIngresantes = new Alumno[62];
        Alumno[] arregloEgresados = new Alumno[500];
        int[] arregloRepitentes = new int[70];

        cargaIngresantes(arregloIngresantes);

        //Carga inicial de datos al sistema.
        cargaAlumnos(matrizAlumnos);
        cargaIngresantes(arregloIngresantes);
        cargaLegajosRepitentes(arregloRepitentes);
        System.out.println("El sistema ha sido exitosamente cargado con alumnos.");

        //Llamado al menu principal:
        menu(matrizAlumnos, arregloEgresados, arregloIngresantes, arregloRepitentes);
    }

}
