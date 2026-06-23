# Aplicativo para el Registro de Historias Clínicas de una Posta

---

## Índice
* [CAPÍTULO I: ASPECTOS GENERALES](#capítulo-i-aspectos-generales)
  * [1.1. Presentación de la Empresa](#11-presentación-de-la-empresa)
  * [1.2. Problemática](#12-problemática)
    * [1.2.1. Problema General](#121-problema-general)
    * [1.2.2. Problemas Específicos](#122-problemas-específicos)
  * [1.3. Objetivos](#13-objetivos)
    * [1.3.1. Objetivo General](#131-objetivo-general)
    * [1.3.2. Objetivos Específicos](#132-objetivos-específicos)
  * [1.4. Alcances](#14-alcances)
  * [1.5. Justificaciones](#15-justificaciones)
    * [1.5.1. Justificación Institucional](#151-justificación-institucional)
    * [1.5.2. Justificación Tecnológica](#152-justificación-tecnológica)
    * [1.5.3. Justificación Operacional](#153-justificación-operacional)
    * [1.5.4. Justificación Procedimental](#154-justificación-procedimental)
* [CAPÍTULO II: MARCO TEÓRICO](#capítulo-ii-marco-teórico)
  * [2.1. Antecedentes](#21-antecedentes)
  * [2.2. Marco Teórico](#22-marco-teórico)
    * [2.2.1. Estructuras de Datos](#221-estructuras-de-datos)
    * [2.2.2. Arreglos Unidimensionales](#222-arreglos-unidimensionales)
    * [2.2.3. Operaciones con Arreglos Unidimensionales](#223-operaciones-con-arreglos-unidimensionales)
    * [2.2.4. Arreglos Bidimensionales](#224-arreglos-bidimensionales)
    * [2.2.5. Tipo Abstracto de Datos (TAD)](#225-tipo-abstracto-de-datos-tad)
    * [2.2.6. Listas Enlazadas Simples](#226-listas-enlazadas-simples)
    * [2.2.7. Operaciones sobre Listas Enlazadas](#227-operaciones-sobre-listas-enlazadas)
    * [2.2.8. Listas Doblemente Enlazadas](#228-listas-doblemente-enlazadas)
    * [2.2.9. Listas Circulares](#229-listas-circulares)
    * [2.2.10. Algoritmos de Búsqueda y Recorrido](#2210-algoritmos-de-búsqueda-y-recorrido)
    * [2.2.11. Programación Orientada a Objetos](#2211-programación-orientada-a-objetos)
* [CAPÍTULO III: DESARROLLO DE LA SOLUCIÓN](#capítulo-iii-desarrollo-de-la-solución)
  * [3.1.1. Lista de Requerimientos Funcionales](#311-lista-de-requerimientos-funcionales)
  * [3.1.2. Lista de Requerimientos No Funcionales](#312-lista-de-requerimientos-no-funcionales)
  * [3.1.3. Prototipos del Proyecto](#313-prototipos-del-proyecto)

---

## CAPÍTULO I: ASPECTOS GENERALES

### 1.1. Presentación de la Empresa
La empresa objeto de estudio corresponde a una posta médica dedicada a brindar servicios de atención primaria de salud a la población. Entre las principales actividades que realiza se encuentran la atención de pacientes, el control de consultas médicas, el seguimiento de tratamientos y la administración de historias clínicas.

La correcta gestión de la información médica constituye un aspecto fundamental para garantizar una atención eficiente y de calidad. Sin embargo, el crecimiento constante de la cantidad de pacientes genera una mayor demanda de mecanismos que permitan almacenar, organizar y consultar información de manera rápida y segura.

Por ello, se propone el desarrollo de un aplicativo para el registro de historias clínicas que permita optimizar la administración de la información médica mediante la aplicación de algoritmos y estructuras de datos estudiadas durante el curso.

### 1.2. Problemática
Actualmente, la gestión de historias clínicas en muchas postas médicas se realiza mediante procedimientos manuales o sistemas que presentan limitaciones para el almacenamiento y consulta de información. Esta situación ocasiona dificultades para localizar rápidamente los datos de los pacientes, incrementa los tiempos de atención y dificulta el seguimiento adecuado de los tratamientos médicos.

Asimismo, la administración manual de la información aumenta el riesgo de pérdida de documentos, duplicidad de registros y errores durante los procesos de actualización de datos. La falta de una herramienta informática especializada también limita la generación de reportes y consultas que permitan apoyar la toma de decisiones dentro de la institución.

#### 1.2.1. Problema General
¿Cómo desarrollar un aplicativo informático que permita gestionar de manera eficiente las historias clínicas de los pacientes de una posta médica mediante la aplicación de algoritmos y estructuras de datos?

#### 1.2.2. Problemas Específicos
* ¿Cómo facilitar el registro y mantenimiento de la información de los pacientes?
* ¿Cómo gestionar adecuadamente la información de los médicos y especialidades médicas?
* ¿Cómo registrar las historias clínicas y las atenciones médicas realizadas?
* ¿Cómo agilizar la búsqueda de pacientes mediante DNI o nombres?
* ¿Cómo consultar las atenciones realizadas por un médico determinado?
* ¿Cómo generar consultas de atenciones por especialidad y rango de fechas?
* ¿Cómo visualizar y generar historias clínicas en formato PDF?

### 1.3. Objetivos

#### 1.3.1. Objetivo General
Desarrollar un aplicativo para el registro de historias clínicas que permita administrar eficientemente la información de pacientes, médicos, especialidades y atenciones médicas dentro de una posta de salud.

#### 1.3.2. Objetivos Específicos
* Implementar un sistema de autenticación mediante login.
* Desarrollar el módulo de gestión de pacientes.
* Desarrollar el módulo de gestión de médicos.
* Implementar el módulo de gestión de especialidades médicas.
* Registrar historias clínicas y atenciones médicas.
* Implementar mecanismos de búsqueda de pacientes por DNI y nombres.
* Consultar las atenciones realizadas por los médicos.
* Generar reportes por especialidad y rango de fechas.
* Visualizar y generar historias clínicas en formato PDF.

### 1.4. Alcances
El proyecto comprende el análisis, diseño y desarrollo de un aplicativo para el registro de historias clínicas de una posta médica. El sistema permitirá administrar información relacionada con pacientes, médicos y especialidades médicas, además de registrar historias clínicas y atenciones realizadas.

También permitirá realizar búsquedas de pacientes mediante DNI o nombres, consultar atenciones por médico y especialidad, generar reportes por rango de fechas y visualizar historias clínicas en formato PDF.

Para el desarrollo del proyecto se aplicarán los conocimientos adquiridos en el curso de Algoritmo y Estructura de Datos, utilizando arreglos unidimensionales, tipos abstractos de datos y listas enlazadas para la organización y procesamiento de la información.

### 1.5. Justificaciones

#### 1.5.1. Justificación Institucional
La implementación del sistema permitirá mejorar la gestión de la información médica dentro de la posta de salud, facilitando el acceso a las historias clínicas y optimizando los procesos administrativos relacionados con el registro y consulta de datos. Asimismo, contribuirá a brindar una atención más rápida y eficiente a los pacientes, mejorando la calidad del servicio profesional ofrecido por la institución.

#### 1.5.2. Justificación Tecnológica
El proyecto permitirá aplicar herramientas de desarrollo de software y conceptos relacionados con algoritmos y estructuras de datos para resolver una problemática real. La utilización de arreglos, listas enlazadas y tipos abstractos de datos permitirá gestionar eficientemente la información almacenada dentro del sistema.

#### 1.5.3. Justificación Operacional
El sistema reducirá el tiempo necesario para registrar, buscar y consultar información médica, facilitando las labores del personal administrativo y médico. Además, permitirá mantener la información organizada y disponible cuando sea requerida durante el proceso de atención al paciente.

#### 1.5.4. Justificación Procedimental
La implementación del aplicativo permitirá estandarizar los procedimientos relacionados con el registro y administración de historias clínicas. Asimismo, contribuirá a disminuir errores asociados al manejo manual de información y mejorará el control de los registros médicos.

---

## CAPÍTULO II: MARCO TEÓRICO

### 2.1. Antecedentes
La transformación digital ha permitido que los centros de salud incorporen sistemas informáticos para mejorar la gestión de información médica. Estos sistemas facilitan el almacenamiento de datos, agilizan las consultas y contribuyen a mejorar la calidad de la atención brindada a los pacientes.

Las historias clínicas electrónicas se han convertido en una herramienta fundamental para registrar antecedentes médicos, diagnósticos, tratamientos y controles realizados durante las consultas. El presente proyecto busca desarrollar un aplicativo orientado a la gestión de historias clínicas aplicando los conocimientos adquiridos en el curso de Algoritmo y Estructura de Datos.

### 2.2. Marco Teórico

#### 2.2.1. Estructuras de Datos
Las estructuras de datos son mecanismos que permiten organizar y almacenar información para facilitar su procesamiento. Dentro del proyecto se utilizarán diferentes estructuras de datos para representar pacientes, médicos, especialidades, historias clínicas y atenciones médicas.

#### 2.2.2. Arreglos Unidimensionales
Los arreglos unidimensionales permiten almacenar elementos del mismo tipo en posiciones consecutivas de memoria. En el sistema se utilizarán arreglos unidimensionales de objetos para almacenar la información de pacientes, médicos y especialidades médicas.

#### 2.2.3. Operaciones con Arreglos Unidimensionales
Las operaciones fundamentales de los arreglos serán utilizadas durante el desarrollo del proyecto:
* **Inserción:** Permitirá registrar nuevos pacientes, médicos y especialidades.
* **Actualización:** Permitirá modificar información existente.
* **Eliminación:** Facilitará retirar registros que ya no sean necesarios.
* **Recorrido:** Permitirá mostrar listados de información.
* **Búsqueda:** Permitirá localizar registros específicos mediante DNI o nombres.
* **Comparación:** Permitirá detectar registros duplicados.
* **Copia y clonación:** Podrán utilizarse para respaldar temporalmente información.

#### 2.2.4. Arreglos Bidimensionales / Organización de Objetos
Los arreglos unidimensionales serán utilizados para almacenar los registros de pacientes, médicos y especialidades. Cada elemento del arreglo contendrá un objeto con la información correspondiente. La inserción permitirá registrar nuevos elementos, la actualización modificará información existente, la eliminación retirará registros, mientras que el recorrido y la búsqueda permitirán mostrar y localizar información específica dentro del sistema.

#### 2.2.5. Tipo Abstracto de Datos (TAD)
Un Tipo Abstracto de Datos define una estructura lógica junto con las operaciones que pueden realizarse sobre ella. Dentro del sistema se utilizarán TAD para representar entidades como *Paciente*, *Médico*, *Especialidad*, *Historia Clínica* y *Atención Médica*.

#### 2.2.6. Listas Enlazadas Simples
Las listas enlazadas simples serán utilizadas para almacenar las historias clínicas debido a que la cantidad de registros puede aumentar constantemente. Cada nodo almacenará una historia clínica y una referencia al siguiente nodo. Esto permitirá agregar nuevos registros dinámicamente sin necesidad de definir un tamaño fijo.

#### 2.2.7. Operaciones sobre Listas Enlazadas
Las listas enlazadas permiten realizar operaciones de inserción, eliminación, búsqueda y recorrido. Estas operaciones facilitarán la administración de historias clínicas y consultas médicas.

#### 2.2.8. Listas Doblemente Enlazadas
Las listas doblemente enlazadas serán utilizadas para administrar el historial de atenciones médicas. Cada nodo almacenará una atención y tendrá referencias tanto al nodo anterior como al siguiente, facilitando la navegación entre registros históricos de un paciente.

#### 2.2.9. Listas Circulares
Las listas circulares permiten realizar recorridos continuos sobre una colección de elementos. Estas estructuras podrán ser utilizadas en futuras mejoras del sistema para optimizar determinados procesos de consulta.

#### 2.2.10. Algoritmos de Búsqueda y Recorrido
Los algoritmos de búsqueda permiten localizar información dentro de una estructura de datos. En el sistema se utilizarán para buscar pacientes mediante DNI o nombres y consultar registros médicos. Los algoritmos de recorrido permitirán mostrar información almacenada y listas enlazadas.

#### 2.2.11. Programación Orientada a Objetos
La Programación Orientada a Objetos permite representar entidades del mundo real mediante clases y objetos. El sistema estará conformado por clases como *Paciente*, *Médico*, *Especialidad*, *Historia Clínica* y *Atención Médica*.

---

## CAPÍTULO III: DESARROLLO DE LA SOLUCIÓN

### 3.1.1. Lista de Requerimientos Funcionales
* **RF01.** El sistema permitirá el acceso mediante login.
* **RF02.** El sistema permitirá navegar entre los distintos formularios del aplicativo.
* **RF03.** El sistema permitirá registrar, actualizar, eliminar y consultar pacientes.
* **RF04.** El sistema permitirá registrar, actualizar, eliminar y consultar médicos.
* **RF05.** El sistema permitirá registrar, actualizar, eliminar y consultar especialidades médicas.
* **RF06.** El sistema permitirá registrar historias clínicas y atenciones médicas.
* **RF07.** El sistema permitirá buscar pacientes mediante DNI o nombres.
* **RF08.** El sistema permitirá visualizar las atenciones realizadas por un médico durante el día.
* **RF09.** El sistema permitirá consultar atenciones por especialidad y rango de fechas.
* **RF10.** El sistema permitirá generar y visualizar historias clínicas en formato PDF.

### 3.1.2. Lista de Requerimientos No Funcionales
* **RNF01.** El sistema deberá presentar una interfaz amigable e intuitiva.
* **RNF02.** El tiempo de respuesta de las consultas deberá ser menor a tres segundos.
* **RNF03.** El sistema deberá garantizar la integridad de la información almacenada.
* **RNF04.** El acceso al sistema deberá estar protegido mediante autenticación de usuarios.
* **RNF05.** El sistema deberá permitir la generación correcta de documentos PDF.
* **RNF06.** El sistema deberá mantener la disponibilidad de la información durante las operaciones de consulta.
* **RNF07.** El sistema deberá facilitar el mantenimiento y actualización del software.
* **RNF08.** El sistema deberá permitir el almacenamiento organizado de los registros clínicos.

### 3.1.3. Prototipos del Proyecto
El sistema contará con interfaces gráficas para los siguientes módulos:
1. Pantalla de Login.
2. Menú Principal.
3. Gestión de Pacientes.
4. Gestión de Médicos.
5. Gestión de Especialidades.
6. Registro de Historias Clínicas.
7. Registro de Atenciones Médicas.
8. Consulta de Pacientes.
9. Reportes por Médico.
10. Reportes por Especialidad.
11. Visualización de Historia Clínica en PDF.
