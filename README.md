# <div align="center">🎲 Lab02 — Proyecciones 3D en OpenGL</div>

<div align="center">

![Java](https://img.shields.io/badge/Java-8-orange?style=for-the-badge&logo=java)
![OpenGL](https://img.shields.io/badge/OpenGL-2.1-blue?style=for-the-badge&logo=opengl)
![JOGL](https://img.shields.io/badge/JOGL-2.4-green?style=for-the-badge)
![NetBeans](https://img.shields.io/badge/NetBeans-IDE%2028-1B6AC6?style=for-the-badge&logo=apache-netbeans-ide)
![Status](https://img.shields.io/badge/Status-Completado-success?style=for-the-badge)

**Laboratorio No. 2 — Informática Gráfica y Visualización**  
Fundación Universitaria Internacional de la Rioja · 2026

</div>

---

## 📌 Descripción

Implementación de un sistema de visualización de **proyecciones 3D** usando OpenGL mediante la biblioteca **JOGL (Java OpenGL)**. La aplicación muestra una ventana dividida en **4 cuadrantes**, cada uno renderizando el mismo cubo tridimensional con una técnica de proyección diferente.

> **Restricción principal:** el ojo permanece en `(0,0,0)` en todo momento — `gluLookAt()` no se usa en ninguna parte del código.

---

## 🖥️ Vista previa

<div align="center">

| ORTOGONAL | GABINETE |
|:---------:|:--------:|
| Proyección paralela pura, sin punto de fuga | glOrtho + shear 30°, L=0.5 |

| PERSPECTIVA SIMÉTRICA | PERSPECTIVA OBLICUA |
|:---------------------:|:-------------------:|
| glFrustum centrado, fovy=40° | glFrustum off-axis (0.3, 0.25) |

</div>

---

## 🔧 Proyecciones implementadas

### 1. 📐 Ortogonal
```
glOrtho(-3.5, 3.5, -3.5, 3.5, 1.0, 15.0)
```
- Rayos visuales **paralelos** y perpendiculares al plano
- Sin punto de fuga ni escorzo
- Conserva proporciones geométricas exactas
- Ideal para CAD y planos de ingeniería

### 2. 📦 Gabinete
```
glOrtho(-3.2, 3.2, ...) + M_shear (30°, L=0.5)
```
- Proyección **oblicua paralela** estándar
- Matriz de cizallamiento aplicada manualmente con `glMultMatrixf`
- Profundidad al 50% de la real
- `cos(30°) × 0.5 = 0.433` · `sin(30°) × 0.5 = 0.25`

### 3. 🔭 Perspectiva Simétrica
```
half = tan(20°) × near = 0.728
glFrustum(-0.728, 0.728, -0.728, 0.728, 2.0, 20.0)
```
- Frustum **centrado** respecto al eje Z
- Un único punto de fuga central
- Simula la percepción visual humana

### 4. 🌀 Perspectiva Oblicua (Off-Axis)
```
glFrustum(-0.428, 1.028, -0.478, 0.978, 2.0, 20.0)
offX=0.3 · offY=0.25
```
- Frustum **descentrado** respecto al eje Z
- Punto de fuga desplazado fuera del centro
- Técnica usada en VR, displays estéreo y entornos CAVE

---

## 🏗️ Arquitectura

```
Laboratorio02
├── main()                          → Inicializa JOGL + JFrame
│   └── SwingUtilities.invokeLater()
│       ├── GLProfile.initSingleton()
│       ├── GLCanvas(caps)
│       └── JFrame → pack() → setVisible()
│
├── init()                          → Configura contexto OpenGL
├── reshape()                       → Actualiza dimensiones W/H
├── display()                       → Loop principal de renderizado
│   ├── proyeccionOrtogonal()       → Cuadrante superior izq
│   ├── proyeccionGabinete()        → Cuadrante superior der
│   ├── proyeccionPerspectivaSimetrica() → Cuadrante inferior izq
│   ├── proyeccionPerspectivaOblicua()   → Cuadrante inferior der
│   └── dibujarSeparadores()        → Líneas divisorias 2D
│
├── dibujarCubo()                   → 6 caras GL_QUADS + aristas
├── aplicarRotacion()               → Traslada -Z, rota 20°X / 35°Y
└── dibujarTexto()                  → Labels 2D con TextRenderer
```

---

## 📁 Estructura del repositorio

```
Lab02-Proyecciones3D-OpenGL-JOGL/
│
├── 📄 Desarrollo Proyecto Alejandro De Mendoza.docx   → Informe Word
├── 📄 Desarrollo Proyecto Alejandro De Mendoza.pdf    → Informe PDF
│
├── 📂 Laboratorio02_Alejandro_De_Mendoza/             → Proyecto NetBeans
│   ├── src/
│   │   └── Laboratorio02.java                         → Código fuente principal
│   ├── lib/                                           → JARs JOGL + DLLs nativos
│   ├── nbproject/
│   │   └── project.properties                         → Configuración del proyecto
│   └── build.xml
│
└── 📂 Libreria_OPEN/                                  → JARs originales del profesor
    ├── jogl-all.jar
    ├── jogl-all-natives-windows-amd64.jar
    ├── gluegen-rt.jar
    └── gluegen-rt-natives-windows-amd64.jar
```

---

## ⚙️ Configuración del entorno

| Parámetro | Valor |
|-----------|-------|
| IDE | Apache NetBeans IDE 28 |
| Java Platform | **Zulu 8.0.492+9 (Java SE 8)** |
| Librería gráfica | JOGL 2.4 (jogl-all + gluegen-rt) |
| OpenGL | 2.1 / Fixed-function pipeline |
| Render backend | WindowsWGL (hardware accelerated) |
| OS | Windows 10/11 (64-bit) |

### JVM Args requeridos (`project.properties`)
```properties
run.jvmargs=-Dsun.java2d.noddraw=true \
            -Dsun.awt.noerasebackground=true \
            -Djava.library.path=lib
```
> ⚠️ Estos flags son **obligatorios** para evitar conflictos entre Java2D y el contexto WGL de OpenGL en Windows.

---

## 🚀 Cómo ejecutar

### Requisitos
- Apache NetBeans IDE (cualquier versión reciente)
- Java SE 8 (Zulu 8 recomendado)

### Pasos
1. Clonar el repositorio
```bash
git clone https://github.com/AlejoTechEngineer/Lab02-Proyecciones3D-OpenGL-JOGL.git
```

2. Abrir NetBeans → **File → Open Project** → seleccionar `Laboratorio02_Alejandro_De_Mendoza`

3. Verificar en **Project Properties → Libraries** que los 4 JARs estén en Classpath:
   - `lib/jogl-all-new.jar`
   - `lib/jogl-all-natives-windows-amd64-new.jar`
   - `lib/gluegen-rt-new.jar`
   - `lib/gluegen-rt-natives-windows-amd64-new.jar`

4. Configurar **Java Platform → Zulu 8** (o cualquier JDK 8)

5. **Run → Run Project** (F6)

---

## 📐 Fundamentos matemáticos

### Matriz Ortogonal `glOrtho(l,r,b,t,n,f)`
```
| 2/(r-l)    0       0     -(r+l)/(r-l) |
|    0    2/(t-b)    0     -(t+b)/(t-b) |
|    0       0    -2/(f-n) -(f+n)/(f-n) |
|    0       0       0          1        |
```

### Matriz Shear Gabinete (column-major OpenGL)
```
| 1    0    cos(30°)×0.5   0 |
| 0    1    sin(30°)×0.5   0 |
| 0    0         1         0 |
| 0    0         0         1 |
```

### Pipeline de transformación
```
v_clip = M_projection × M_modelview × v_object
```

---

## ✅ Requisitos cumplidos

- [x] Ventana dividida en 4 cuadrantes con `glViewport()`
- [x] Ojo en `(0,0,0)` — sin `gluLookAt()` en ningún lugar
- [x] Proyección ortogonal con `glOrtho`
- [x] Proyección gabinete con `glOrtho` + matriz shear manual
- [x] Perspectiva simétrica con `glFrustum` centrado
- [x] Perspectiva oblicua con `glFrustum` off-axis
- [x] Etiquetas 2D por cuadrante con `TextRenderer`
- [x] Redimensionado dinámico con callback `reshape()`
- [x] Cierre limpio con tecla `ESC`

---

## 👤 Autor

**Ing. Alejandro De Mendoza**  
Ingeniería Informática 
Fundación Universitaria Internacional de la Rioja  
Bogotá D.C. · 2026

---

<div align="center">

*Laboratorio No. 2 — Informática Gráfica y Visualización*  
*Profesor: Ing. Rogerio Orlando Beltrán Castro*

</div>
