# POOmerica
## Sistema de Gestión Bancaria – Gugu Gaga

POOmerica es una aplicación desarrollada en Java la cual permite simular una gestión de cuentas bancarias. En esta, se pueden realizar distintas operaciones financieras y consultar el historial de movimientos de cada usuario, todo a través de una interfaz en la consola. Solventando la necesidad de gestionar transacciones de forma accesible, digital y organizada.

----------

### 🚀 Funciones
-   **Gestion de cuentas:** Puedes crear y navegar entre las cuentas. Estas pueden ser de tres tipos: estándar, ahorros y corriente. 
-   **Limites:** Existen limites de retiro en las cuentas de ahorro y sobregiros en las cuentas corrientes.
 
-   **Historial de movimientos:** Marca con tiempo real la hora exacta de cada transacción con fecha, hora y una breve descripción del movimiento realizado.
    

----------

### 🧠 Principios

Ya que es una aplicación de simulación para la materia de POO, también quisimos resaltar como se implementan principios importantes:

1.  **Herencia:** Se segmenta y jerarquiza el orden de las clases de manera lógica, siendo “Cuenta” la base y “CuentaAhorros” y “CuentaCorriente” se heredan de esta, teniendo atributos comunes como el historial de transacciones y el titular de la cuenta, pero añadiendo resultados especializados.
    
2.  **Polimorfismo:** Permite que el código trate de forma general las “cuentas” y al mismo ejecuta la lógica especifica de su tipo, sin la necesidad de mutliples if-else. Este principio se puede ver reflejado cuando el usuario solicita un retiro; si la cuenta es corriente, este principio permite que se ejecute la lógica de sobregiro. En cambio si es de ahorros, se aplica la restricción de limite diario.
    
3.  **Encapsulamiento:** Este adquiere vital importancia, especialmente en este proyecto, ya que se utiliza para proteger la integridad de los datos sensibles. Se definieron como `private`/`protected` atributos como el código, cliente y saldo, los cuales solo se modifican con las validaciones necesarias.
    
4.  **Abstraccion:** Permite que aspectos técnicos se conviertan en cosas lógicas para el usuario final. El usuario interactúa con el apartado de “Transferir” sin la necesidad de conocer la lógica interna sobre el sistema.
    

----------

### 🛠️ Manejo de errores

Al estar trabajando en un sistema de gestión bancaria, el tema de los errores e inconsistencias es sumamente importante. Es por eso que se implementan capas de validación para evitar estos problemas, por ejemplo cuando se utiliza el `try-catch` en el `Main` para utilizar la excepción `NumberFormatException` y que si el usuario ingresa texto en lugar de un entero el programa no se cierre, y mande un mensaje de error claro para redirigir al usuario.

----------

### 💻 Instrucciones de uso

**Pre-requisitos:** Tener instalado Java JDK 17 o una versión superior.

Al completar la descarga y compilación del código, el sistema carga 4 cuentas automáticas de ejemplo para pruebas inmediatas sin la necesidad de crear nuevos datos.

Para utilizar el codigo junto a todas sus funciones puedes seguir los siguientes pasos:

1.  **Crear una cuenta nueva:** Esta es la opción 5, el sistema solicitara un código único y nombre del usuario. Se seleccionara el tipo de cuenta (estándar, ahorro o corriente).
    
2.  **Realizar un deposito:** Esta es la opción 1, añade la cantidad de dinero que desees a tu cuenta. Dependiendo el tipo de cuenta se pueden verificar los limites:
    
    -   Intenta retirar $5,500 de una cuenta de ahorro. Se espera un mensaje que indique que el límite por retiro es de $5000 y se niegue la operación.
        
    -   Intenta retirar $500 de una cuenta del tipo corriente sin saldo, ahora el nuevo saldo debería ser -$500 con la descripción de “retiro con sobregiro”.
        
3.  **Transferencia entre cuentas:** Esta es la opción 3, transfiere la cantidad de dinero que desees de una cuenta a la otra. Se espera que se cambie automáticamente el valor de el saldo en cada cuenta.
    
4.  **Consulta de historial:** Esta es la opción 4, y deben aparecer los movimientos junto a la fecha y hora exacta de cuando se realizaron.
    
5.  **Salida:** Esta es la opción 0, se debería mostrar un mensaje de despedida y cierre de menú de forma adecuado.
    

----------

### 👥 Contribuyentes

Este proyecto fue realizado por el equipo Gugu Gaga, compuesto por:

-   Gabriel Méndez [⛩️](https://github.com/GGaboBD)
    
-   Gabriela Méndez  [🦥](https://github.com/gabyyymndz-droid)
    
-   Rodrigo Medrano [🐼](https://github.com/romedrano1105)
- Fernando Calvo [👤](https://github.com/mrdemon05)
- Franco Rosales y Rosales 👤
    

----------

Muchas gracias por utilizar nuestro código. Esperamos este Readme haya sido de ayuda para comprender mejor nuestro código y exprimirlo al 100%. La gestión bancaria es algo del día a día, y lograr hacerla accesible e intuitiva para el usuario, puede ayudar a generar consciencia sobre los gastos y movimientos reales que se realizan a nivel personal, gestionando mejor los gastos y operaciones realizadas de cada cliente.
