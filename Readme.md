# Plan de Desarrollo — Aplicativo de Historias Clínicas (Posta Médica)

> Proyecto del curso **Algoritmo y Estructura de Datos**.
> Stack decidido: **Java + Swing** · Persistencia en **archivos** · Estructuras de datos **implementadas a mano**.

---

## 1. Objetivo y enfoque

Construir una app de escritorio (formularios Swing) que gestione pacientes, médicos,
especialidades, historias clínicas y atenciones, con login, búsquedas, reportes y exportación a PDF.

El **núcleo evaluable** del curso es que las estructuras de datos (arreglo dinámico, lista enlazada
simple, lista doble, lista circular) y sus operaciones estén programadas desde cero, sin usar
`ArrayList`, `LinkedList` ni colecciones del JDK para almacenar las entidades del dominio.

---

## 2. Decisiones técnicas

| Tema | Decisión |
|---|---|
| Lenguaje | Java 17+ (LTS) |
| GUI | Swing (formularios `JFrame`/`JPanel`) |
| Build / dependencias | Maven (recomendado) — sólo se necesita 1 dependencia externa: PDF |
| Librería PDF | **OpenPDF** (licencia libre LGPL/MPL; alternativa a iText 7 que es AGPL) |
| Persistencia | Archivos: serialización binaria `ObjectOutputStream`/`ObjectInputStream` (`.dat`) por repositorio |
| Estructuras de datos | Genéricas y propias (`ArregloDinamico<T>`, `ListaSimple<T>`, `ListaDoble<T>`, `ListaCircular<T>`) |
| Patrón | Capas: `model` → `datastructures` → `repository` → `service` → `ui` |

> Si el profesor prohíbe librerías externas para PDF, alternativa: generar el PDF "a mano" con
> impresión Java2D / `Printable`, o exportar HTML y abrirlo. Se deja OpenPDF como camino principal.

---

## 3. Arquitectura / estructura de paquetes

```
historias-clinicas/
├── pom.xml
└── src/main/java/com/posta/
    ├── Main.java                  # arranca LoginFrame
    ├── datastructures/            # === NÚCLEO DEL CURSO (a mano) ===
    │   ├── Nodo.java              # nodo simple (dato + siguiente)
    │   ├── NodoDoble.java         # nodo doble (anterior + dato + siguiente)
    │   ├── ArregloDinamico.java   # arreglo unidimensional redimensionable <T>
    │   ├── ListaSimple.java       # lista enlazada simple <T>
    │   ├── ListaDoble.java        # lista doblemente enlazada <T>
    │   └── ListaCircular.java     # lista circular <T> (mejora futura, doc 2.2.9)
    ├── model/                     # TAD / entidades POO (Serializable)
    │   ├── Usuario.java
    │   ├── Especialidad.java
    │   ├── Medico.java
    │   ├── Paciente.java
    │   ├── HistoriaClinica.java   # contiene ListaDoble<Atencion>
    │   └── Atencion.java
    ├── repository/                # almacenan entidades en estructuras propias + persistencia
    │   ├── RepositorioBase.java   # guardar()/cargar() genérico a archivo
    │   ├── RepositorioPaciente.java       # ArregloDinamico<Paciente>
    │   ├── RepositorioMedico.java         # ArregloDinamico<Medico>
    │   ├── RepositorioEspecialidad.java   # ArregloDinamico<Especialidad>
    │   └── RepositorioHistoria.java       # ListaSimple<HistoriaClinica>
    ├── service/                   # lógica de negocio
    │   ├── AuthService.java       # login (RF01)
    │   ├── BusquedaService.java   # buscar por DNI / nombre (RF07)
    │   ├── AtencionService.java   # registrar atención, atenciones del día por médico (RF06, RF08)
    │   ├── ReporteService.java    # por especialidad + rango de fechas (RF09)
    │   └── PdfService.java        # generar historia clínica en PDF (RF10)
    ├── ui/                        # formularios Swing
    │   ├── LoginFrame.java
    │   ├── MenuPrincipalFrame.java
    │   ├── PacientesFrame.java
    │   ├── MedicosFrame.java
    │   ├── EspecialidadesFrame.java
    │   ├── HistoriaClinicaFrame.java
    │   ├── AtencionFrame.java
    │   ├── BuscarPacienteFrame.java
    │   ├── ReporteMedicoFrame.java
    │   └── ReporteEspecialidadFrame.java
    └── util/                      # utilidades
        ├── ValidadorDni.java
        ├── FechaUtil.java
        └── ArchivoUtil.java       # rutas de archivos /data
```

Carpeta `data/` (junto al ejecutable) para los `.dat`: `pacientes.dat`, `medicos.dat`,
`especialidades.dat`, `historias.dat`, `usuarios.dat`.

---

## 4. Mapeo entidades ↔ estructuras de datos (lo que pide el documento)

| Entidad | Estructura usada | Justificación (doc) |
|---|---|---|
| Paciente, Médico, Especialidad | `ArregloDinamico<T>` | Arreglos unidimensionales de objetos (2.2.2 – 2.2.4) |
| Historias clínicas (colección global) | `ListaSimple<HistoriaClinica>` | Crecen constantemente, tamaño no fijo (2.2.6) |
| Atenciones de un paciente (historial) | `ListaDoble<Atencion>` dentro de cada `HistoriaClinica` | Navegación anterior/siguiente del historial (2.2.8) |
| (Mejora futura) recorridos continuos | `ListaCircular<T>` | Reservada para optimizaciones (2.2.9) |

Operaciones a implementar en cada estructura: **insertar, actualizar, eliminar, buscar, recorrer,
comparar (duplicados), clonar/copiar** (cubre 2.2.3 y 2.2.7).

---

## 5. Trazabilidad de requerimientos funcionales

| RF | Descripción | Módulo / Pantalla | Estructura / algoritmo |
|---|---|---|---|
| RF01 | Login | `LoginFrame` + `AuthService` | Búsqueda en `ArregloDinamico<Usuario>` |
| RF02 | Navegación entre formularios | `MenuPrincipalFrame` | — |
| RF03 | CRUD pacientes | `PacientesFrame` + `RepositorioPaciente` | `ArregloDinamico<Paciente>` |
| RF04 | CRUD médicos | `MedicosFrame` + `RepositorioMedico` | `ArregloDinamico<Medico>` |
| RF05 | CRUD especialidades | `EspecialidadesFrame` | `ArregloDinamico<Especialidad>` |
| RF06 | Registrar historias y atenciones | `HistoriaClinicaFrame`, `AtencionFrame` | `ListaSimple` + `ListaDoble` |
| RF07 | Buscar paciente por DNI / nombre | `BuscarPacienteFrame` + `BusquedaService` | Búsqueda lineal sobre arreglo |
| RF08 | Atenciones de un médico en el día | `ReporteMedicoFrame` | Recorrido + filtro por fecha/médico |
| RF09 | Atenciones por especialidad y rango de fechas | `ReporteEspecialidadFrame` | Recorrido + filtro por rango |
| RF10 | Generar/visualizar historia clínica en PDF | `PdfService` | Recorrido `ListaDoble<Atencion>` → PDF |

No funcionales relevantes: respuesta < 3 s (RNF02 — búsquedas lineales bastan al volumen esperado),
integridad de datos (RNF03 — validaciones + guardado atómico), auth (RNF04), PDF correcto (RNF05).

---

## 6. Fases de desarrollo (orden recomendado)

### Fase 0 — Setup
- Crear proyecto Maven, JDK 17, `pom.xml` con dependencia OpenPDF.
- Estructura de paquetes vacía + `Main` que abre una ventana de prueba.
- **Entregable:** proyecto compila y corre.

### Fase 1 — Estructuras de datos a mano
- `ArregloDinamico<T>`: capacidad inicial, redimensionar al llenarse, `insertar`, `obtener(i)`,
  `actualizar(i)`, `eliminar(i)`, `buscar(predicado)`, `recorrer`, `tamaño`, `contiene`, `clonar`.
- `ListaSimple<T>` y `ListaDoble<T>`: `insertarInicio/Final`, `eliminar`, `buscar`, `recorrer`,
  `tamaño`, iteración; en la doble, navegación bidireccional.
- `ListaCircular<T>` (mínima, para cumplir el marco teórico).
- **Pruebas unitarias** (JUnit) de cada operación. Esta fase es la más evaluada: dejarla sólida.
- **Entregable:** estructuras testeadas e independientes de la UI.

### Fase 2 — Modelo / TAD
- Clases `Usuario`, `Especialidad`, `Medico`, `Paciente`, `HistoriaClinica`, `Atencion`.
- Todas `implements Serializable` con `serialVersionUID`.
- `HistoriaClinica` contiene `ListaDoble<Atencion>` (las estructuras propias también `Serializable`).
- `equals`/`hashCode` por clave natural (DNI, id) para detección de duplicados.
- **Entregable:** modelo de dominio completo.

### Fase 3 — Repositorios + persistencia en archivos
- `RepositorioBase`: `guardar()` y `cargar()` con `ObjectOutputStream`/`ObjectInputStream`.
- Un repositorio por entidad, usando la estructura correspondiente.
- Cargar al iniciar la app, guardar tras cada alta/baja/modificación (guardado seguro: escribir a
  archivo temporal y renombrar, para no corromper datos — RNF03).
- Datos semilla: usuario admin por defecto, especialidades base.
- **Entregable:** los datos sobreviven al cierre de la app.

### Fase 4 — Servicios / lógica
- `AuthService.login(usuario, clave)` (RF01).
- `BusquedaService` por DNI y por nombre/apellidos parcial (RF07).
- `AtencionService.registrar(...)` y `atencionesDelDia(medico, fecha)` (RF06, RF08).
- `ReporteService.porEspecialidadYRango(esp, desde, hasta)` (RF09).
- **Entregable:** lógica probada desde un `main` de consola antes de la UI.

### Fase 5 — Interfaz Swing
- `LoginFrame` → `MenuPrincipalFrame` (barra de menú con navegación, RF02).
- CRUDs `Pacientes`, `Medicos`, `Especialidades`: `JTable` + formulario alta/edición + validaciones.
- `HistoriaClinicaFrame` + `AtencionFrame`: abrir/crear historia de un paciente y agregar atenciones.
- `BuscarPacienteFrame`, `ReporteMedicoFrame`, `ReporteEspecialidadFrame`.
- Validaciones de UI: DNI de 8 dígitos, campos obligatorios, fechas válidas (RNF01).
- **Entregable:** flujo completo navegable.

### Fase 6 — Generación de PDF
- `PdfService.generarHistoriaClinica(historia)`: cabecera de la posta, datos del paciente,
  tabla de atenciones (recorriendo la `ListaDoble`), guardar `.pdf` y abrirlo con `Desktop.open` (RF10).
- Reportes RF08/RF09 también exportables a PDF (opcional pero suma).
- **Entregable:** PDF generado y abierto desde la app.

### Fase 7 — Integración, pruebas y pulido
- Datos de prueba realistas, recorrido de todos los RF, manejo de errores y mensajes claros.
- Revisión de tiempos (RNF02), README de uso y de compilación, capturas para el informe.
- **Entregable:** versión presentable + documentación.

**Estimado total:** ~10 días de trabajo (ajustable según equipo).

---

## 7. Riesgos y notas

- **Serializar estructuras propias:** marcar `Nodo`/`NodoDoble` y las listas como `Serializable`, o
  bien persistir un "snapshot" (escribir el conteo + cada objeto) y reconstruir la estructura al
  cargar. La segunda opción evita acoplar el formato de archivo a la clase de la estructura.
- **Referencias entre entidades:** guardar en `Atencion`/`Medico` el **id** de la especialidad (no el
  objeto) para evitar duplicación al persistir; resolver el objeto vía repositorio al mostrar.
- **Concurrencia Swing:** todo el trabajo pesado fuera del Event Dispatch Thread si crece; al volumen
  académico no es crítico.
- **Licencia PDF:** usar OpenPDF (libre). Evitar iText 7 si el trabajo se distribuye.

---

## 8. Primeros pasos concretos

1. Confirmar JDK 17 instalado (`java -version`).
2. Crear el proyecto Maven y el `pom.xml` con OpenPDF.
3. Implementar y testear `ArregloDinamico<T>` y `ListaSimple<T>` (Fase 1) — empezar por aquí.
