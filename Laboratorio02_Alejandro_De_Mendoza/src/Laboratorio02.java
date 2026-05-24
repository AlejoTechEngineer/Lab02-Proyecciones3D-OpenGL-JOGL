/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Alejo
 */
/*
 * Laboratorio 2 - Cuatro Proyecciones 3D en OpenGL
 * Informatica Grafica y Visualizacion
 *
 * Ventana dividida en 4 cuadrantes:
 *   [Superior izq]  Proyeccion Ortogonal
 *   [Superior der]  Proyeccion Gabinete
 *   [Inferior izq]  Proyeccion Perspectiva Simetrica
 *   [Inferior der]  Proyeccion Perspectiva Oblicua
 *
 * RESTRICCION: el ojo permanece en (0,0,0) - sin gluLookAt().
 */
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.awt.TextRenderer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Laboratorio02 implements GLEventListener, KeyListener {

    static int W = 900;
    static int H = 900;
    private TextRenderer renderer;

    // ─────────────────────────────────────────────
    // MAIN
    // ─────────────────────────────────────────────
    public static void main(String[] args) {
    System.setProperty("sun.java2d.noddraw", "true");
    System.setProperty("sun.awt.noerasebackground", "true");  
    System.setProperty("jogl.disable.openglarbcontext", "true");
    
        SwingUtilities.invokeLater(() -> {
            GLProfile.initSingleton();
            GLProfile profile = GLProfile.get(GLProfile.GL2);
            GLCapabilities caps = new GLCapabilities(profile);
            caps.setDoubleBuffered(true);
            caps.setHardwareAccelerated(true);

            GLCanvas canvas = new GLCanvas(caps);
            canvas.setPreferredSize(new Dimension(W, H));

            Laboratorio02 app = new Laboratorio02();
            canvas.addGLEventListener(app);
            canvas.addKeyListener(app);

            JFrame frame = new JFrame("Lab2 - Cuatro Proyecciones 3D");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(canvas);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            canvas.requestFocusInWindow();
        });
    }

    // ─────────────────────────────────────────────
    // INIT
    // ─────────────────────────────────────────────
    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.07f, 0.07f, 0.09f, 1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        renderer = new TextRenderer(new Font("Arial", Font.BOLD, 14));
    }

    // ─────────────────────────────────────────────
    // RESHAPE
    // ─────────────────────────────────────────────
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        if (h == 0) h = 1;
        W = w;
        H = h;
    }

    // ─────────────────────────────────────────────
    // DISPLAY
    // ─────────────────────────────────────────────
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        int hw = W / 2;
        int hh = H / 2;

        proyeccionOrtogonal(gl, 0, hh, hw, hh);
        proyeccionGabinete(gl, hw, hh, hw, hh);
        proyeccionPerspectivaSimetrica(gl, 0, 0, hw, hh);
        proyeccionPerspectivaOblicua(gl, hw, 0, hw, hh);

        gl.glDisable(GL2.GL_DEPTH_TEST);
        dibujarSeparadores(gl);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glFlush();
    }

    // ─────────────────────────────────────────────
    // CUBO: lado 2, centrado en origen
    // ─────────────────────────────────────────────
    private void dibujarCubo(GL2 gl) {
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(0.9f, 0.15f, 0.15f);
        gl.glVertex3f(-1,-1, 1); gl.glVertex3f( 1,-1, 1);
        gl.glVertex3f( 1, 1, 1); gl.glVertex3f(-1, 1, 1);
        gl.glColor3f(0.15f, 0.35f, 0.9f);
        gl.glVertex3f(-1,-1,-1); gl.glVertex3f(-1, 1,-1);
        gl.glVertex3f( 1, 1,-1); gl.glVertex3f( 1,-1,-1);
        gl.glColor3f(0.1f, 0.82f, 0.2f);
        gl.glVertex3f(-1, 1,-1); gl.glVertex3f(-1, 1, 1);
        gl.glVertex3f( 1, 1, 1); gl.glVertex3f( 1, 1,-1);
        gl.glColor3f(0.95f, 0.85f, 0.1f);
        gl.glVertex3f(-1,-1,-1); gl.glVertex3f( 1,-1,-1);
        gl.glVertex3f( 1,-1, 1); gl.glVertex3f(-1,-1, 1);
        gl.glColor3f(0.1f, 0.85f, 0.9f);
        gl.glVertex3f( 1,-1,-1); gl.glVertex3f( 1, 1,-1);
        gl.glVertex3f( 1, 1, 1); gl.glVertex3f( 1,-1, 1);
        gl.glColor3f(0.9f, 0.15f, 0.85f);
        gl.glVertex3f(-1,-1,-1); gl.glVertex3f(-1,-1, 1);
        gl.glVertex3f(-1, 1, 1); gl.glVertex3f(-1, 1,-1);
        gl.glEnd();

        gl.glColor3f(0, 0, 0);
        gl.glLineWidth(1.8f);
        gl.glBegin(GL2.GL_LINE_LOOP);
        gl.glVertex3f(-1,-1, 1); gl.glVertex3f( 1,-1, 1);
        gl.glVertex3f( 1, 1, 1); gl.glVertex3f(-1, 1, 1);
        gl.glEnd();
        gl.glBegin(GL2.GL_LINE_LOOP);
        gl.glVertex3f(-1,-1,-1); gl.glVertex3f( 1,-1,-1);
        gl.glVertex3f( 1, 1,-1); gl.glVertex3f(-1, 1,-1);
        gl.glEnd();
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex3f(-1,-1, 1); gl.glVertex3f(-1,-1,-1);
        gl.glVertex3f( 1,-1, 1); gl.glVertex3f( 1,-1,-1);
        gl.glVertex3f( 1, 1, 1); gl.glVertex3f( 1, 1,-1);
        gl.glVertex3f(-1, 1, 1); gl.glVertex3f(-1, 1,-1);
        gl.glEnd();
    }

    // ─────────────────────────────────────────────
    // Rotacion comun: 20 deg X, 35 deg Y
    // ─────────────────────────────────────────────
    private void aplicarRotacion(GL2 gl, float tz) {
        gl.glTranslatef(0.0f, 0.0f, tz);
        gl.glRotatef(20.0f, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(35.0f, 0.0f, 1.0f, 0.0f);
    }

    // ─────────────────────────────────────────────
    // Texto 2D superpuesto en el cuadrante
    // ─────────────────────────────────────────────
    private void dibujarTexto(int vw, int vh, int px, int py, String texto) {
        renderer.beginRendering(vw, vh);
        renderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        renderer.draw(texto, px, py);
        renderer.endRendering();
    }

    // ─────────────────────────────────────────────
    // 1. ORTOGONAL
    // ─────────────────────────────────────────────
    private void proyeccionOrtogonal(GL2 gl, int x, int y, int w, int h) {
        gl.glViewport(x, y, w, h);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-3.5, 3.5, -3.5, 3.5, 1.0, 15.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        aplicarRotacion(gl, -5.0f);
        dibujarCubo(gl);
        dibujarTexto(w, h, 10, 14, "ORTOGONAL");
    }

    // ─────────────────────────────────────────────
    // 2. GABINETE
    // ─────────────────────────────────────────────
    private void proyeccionGabinete(GL2 gl, int x, int y, int w, int h) {
        gl.glViewport(x, y, w, h);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-3.2, 3.2, -3.2, 3.2, 1.0, 15.0);
        double ang = 30.0 * Math.PI / 180.0;
        double L   = 0.5;
        float[] shear = {
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            (float)(Math.cos(ang)*L), (float)(Math.sin(ang)*L), 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        };
        gl.glMultMatrixf(shear, 0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(2.165f, 1.25f, 0.0f);
        aplicarRotacion(gl, -5.0f);
        dibujarCubo(gl);
        dibujarTexto(w, h, 10, 14, "GABINETE");
    }

    // ─────────────────────────────────────────────
    // 3. PERSPECTIVA SIMETRICA
    // ─────────────────────────────────────────────
    private void proyeccionPerspectivaSimetrica(GL2 gl, int x, int y, int w, int h) {
        gl.glViewport(x, y, w, h);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        double near = 2.0;
        double half = Math.tan(20.0 * Math.PI / 180.0) * near;
        gl.glFrustum(-half, half, -half, half, near, 20.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        aplicarRotacion(gl, -9.0f);
        dibujarCubo(gl);
        dibujarTexto(w, h, 10, 14, "PERSPECTIVA SIMETRICA");
    }

    // ─────────────────────────────────────────────
    // 4. PERSPECTIVA OBLICUA
    // ─────────────────────────────────────────────
    private void proyeccionPerspectivaOblicua(GL2 gl, int x, int y, int w, int h) {
        gl.glViewport(x, y, w, h);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        double near = 2.0;
        double half = Math.tan(20.0 * Math.PI / 180.0) * near;
        double offX = 0.3;
        double offY = 0.25;
        gl.glFrustum(-half + offX, half + offX,
                     -half + offY, half + offY,
                      near, 20.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(1.35f, 1.125f, 0.0f);
        aplicarRotacion(gl, -9.0f);
        dibujarCubo(gl);
        dibujarTexto(w, h, 10, 14, "PERSPECTIVA OBLICUA");
    }

    // ─────────────────────────────────────────────
    // Separadores entre cuadrantes
    // ─────────────────────────────────────────────
    private void dibujarSeparadores(GL2 gl) {
        gl.glViewport(0, 0, W, H);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, W, 0, H, -1, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glColor3f(0.45f, 0.45f, 0.45f);
        gl.glLineWidth(2.0f);
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex2i(W/2, 0);   gl.glVertex2i(W/2, H);
        gl.glVertex2i(0,   H/2); gl.glVertex2i(W,   H/2);
        gl.glEnd();
    }

    // ─────────────────────────────────────────────
    // DISPOSE
    // ─────────────────────────────────────────────
    @Override
    public void dispose(GLAutoDrawable drawable) {
        if (renderer != null) renderer.dispose();
    }

    // ─────────────────────────────────────────────
    // TECLADO
    // ─────────────────────────────────────────────
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) System.exit(0);
    }
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}