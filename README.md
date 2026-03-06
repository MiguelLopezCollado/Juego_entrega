# Gato vs Ovillos - Juego de Entrega

## 1. Introducción
**Nombre del juego:** Gato vs Ovillos  
**Temática:** En este juego, el jugador controla a un simpático **gato** (representado por el `bucket`) que debe recolectar **ovillos de lana** (las `drops`) que caen del cielo.  
**Objetivo:** El objetivo principal es atrapar la mayor cantidad de ovillos posible para sumar puntos. Si un ovillo toca el suelo, el gato pierde una vida. El juego termina cuando se agotan las 3 vidas iniciales.

## 2. Desarrollo
### Lógica del Juego
- **Colisiones:** Se utiliza la función `overlaps()` entre los rectángulos de colisión (`getBoundingRectangle()`) del gato y los ovillos. Al detectar contacto, el ovillo se elimina, se suma un punto y se reproduce un sonido.
- **Movimiento:** El gato se desplaza horizontalmente mediante las teclas de dirección (izquierda/derecha) o por entrada táctil/ratón, utilizando `unproject()` para sincronizar las coordenadas de pantalla con las del mundo.
- **Dificultad Progresiva:** Cada vez que el jugador atrapa un ovillo, la velocidad de caída de los siguientes aumenta ligeramente, incrementando el reto conforme avanza la partida.
- **Gestión de Vidas:** Se implementó un contador de vidas que, al llegar a cero, activa un estado de "Game Over" bloqueando la lógica de movimiento y permitiendo el reinicio con la tecla ESPACIO.

### Estructura del Juego
- **Clases:** La lógica reside principalmente en la clase `Main.java`, que implementa `ApplicationListener`.
- **Componentes:** Se utiliza `SpriteBatch` para el renderizado, `FitViewport` para mantener la relación de aspecto y `BitmapFont` para la interfaz de usuario (marcador y vidas).
- **Recursos:** Los activos se cargan como `Texture` (PNG) en el método `create()` y se liberan en `dispose()`.

## 3. Conclusiones
Durante el desarrollo se ha profundizado en la **diferencia entre la representación lógica y la gráfica**:
- Mientras que gráficamente vemos un gato y ovillos con texturas detalladas, la lógica del juego "ve" simplemente rectángulos matemáticos moviéndose en un plano. 
- Entender esta separación es vital para optimizar el rendimiento y asegurar que las interacciones (como las colisiones) sean precisas independientemente de cómo se vea el dibujo final.

---


A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an empty `ApplicationListener` implementation.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
