package com.example.examen_app

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ViewContainer() }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 900)
@Composable
fun ViewContainer() {
    Scaffold(floatingActionButton = { FAB() }) { innerPadding ->
        Content(Modifier.padding(innerPadding))
    }
}

@Composable
fun Content(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Portada + logo + foto de perfil encima
        Box {
            Image(
                painter = painterResource(R.drawable.portada),
                contentDescription = "Portada",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Image(
                painter = painterResource(R.drawable.logoupp),
                contentDescription = "Logo UPP",
                modifier = Modifier
                    .padding(10.dp)
                    .size(50.dp)
                    .background(Color.White, CircleShape)
            )
            Image(
                painter = painterResource(R.drawable.foto_perfil),
                contentDescription = "Foto de perfil",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = 20.dp, y = 50.dp)
                    .size(110.dp)
                    .clip(CircleShape)
                    .border(3.dp, Color.White, CircleShape)
            )
        }

        Spacer(Modifier.height(60.dp))

        About()

        Spacer(Modifier.height(20.dp))

        Etiquetas()

        Spacer(Modifier.height(20.dp))

        Mapa()

        // Para que el FAB no tape el mapa
        Spacer(Modifier.height(80.dp))
    }
}

@Composable
fun About() {
    Column(Modifier.padding(horizontal = 20.dp)) {
        Text("Miguel Angel Guerrero Lopez", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("guerrerolopezma97@micorreo.upp.edu.mx", color = colorResource(R.color.morado_upp))
        Spacer(Modifier.height(8.dp))
        Text("Estudiante de la Universidad Politecnica de Pachuca. Me gusta el desarrollo de Software y la programacion.")
    }
}

val etiquetas = listOf(
    "Python", "Java", "Kotlin", "C#", "C++",
    "JavaScript", "PHP", "SQL", "HTML", "CSS"
)

@Composable
fun Etiquetas() {
    Text(
        "Lenguajes",
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
    Spacer(Modifier.height(8.dp))
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(etiquetas) { etiqueta ->
            Text(
                etiqueta,
                color = Color.White,
                modifier = Modifier
                    .background(colorResource(R.color.morado_upp), RoundedCornerShape(50))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

// Ubicacion: 19.979820869796345, -98.6846698039993
val mapaHtml = """
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css">
  <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
</head>
<body style="margin:0">
  <div id="mapa" style="height:250px"></div>
  <script>
    var mapa = L.map('mapa').setView([19.979820869796345, -98.6846698039993], 16);
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap'
    }).addTo(mapa);
    L.marker([19.979820869796345, -98.6846698039993]).addTo(mapa);
  </script>
</body>
</html>
"""

// El mapa no se ve en la Preview, solo en el emulador o celular (necesita internet)
@Composable
fun Mapa() {
    AndroidView(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(250.dp),
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                // El mapa (Leaflet) necesita JavaScript para dibujarse
                settings.javaScriptEnabled = true
                // Dibujar por software: con GPU el emulador deja toda la pantalla en blanco
                setLayerType(android.view.View.LAYER_TYPE_SOFTWARE, null)
                // Leaflet usa imagenes normales; el embed de openstreetmap.org usa WebGL y no se ve
                loadDataWithBaseURL("https://unpkg.com", mapaHtml, "text/html", "UTF-8", null)
            }
        }
    )
}

@Composable
fun FAB() {
    val context = LocalContext.current
    FloatingActionButton(
        onClick = { Toast.makeText(context, "Hola!", Toast.LENGTH_SHORT).show() },
        containerColor = colorResource(R.color.morado_upp),
        contentColor = Color.White
    ) {
        Text("+", fontSize = 26.sp)
    }
}
