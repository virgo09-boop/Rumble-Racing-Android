package com.aether.rumblepocket

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private val pickIsoRequest = 4101
    private val prefs by lazy { getSharedPreferences("rumble_pocket", MODE_PRIVATE) }
    private var isoUri: Uri? = null
    private lateinit var isoStatus: TextView
    private lateinit var playButton: Button
    private lateinit var settingsStatus: TextView
    private lateinit var cheatStatus: TextView

    private val bg = Color.rgb(12, 17, 27)
    private val panel = Color.rgb(25, 34, 48)
    private val cyan = Color.rgb(91, 207, 255)
    private val white = Color.WHITE
    private val muted = Color.rgb(180, 190, 204)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isoUri = prefs.getString("iso_uri", null)?.let(Uri::parse)
        buildUi()
        refreshIsoStatus()
    }

    private fun buildUi() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(22), dp(20), dp(24))
            setBackgroundColor(bg)
        }

        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val eyebrow = text("SINGLE-GAME PS2 LAUNCHER", 11, cyan, true).apply {
            letterSpacing = 0.12f
            gravity = Gravity.CENTER
        }
        content.addView(eyebrow, matchWrap())

        content.addView(text("RUMBLE\nRACING", 34, white, true).apply {
            gravity = Gravity.CENTER
            letterSpacing = 0.04f
            setPadding(0, dp(14), 0, dp(4))
        }, matchWrap())
        content.addView(text("POCKET EDITION  •  BUILD 0.2.0", 12, muted, true).apply {
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, dp(18))
        }, matchWrap())

        val gameCard = panel()
        gameCard.addView(sectionTitle("GAME ISO"), matchWrap())
        isoStatus = text("Pilih file ISO Rumble Racing milikmu.", 14, white, false).apply {
            setPadding(0, dp(8), 0, dp(12))
        }
        gameCard.addView(isoStatus, matchWrap())
        gameCard.addView(button("PILIH FILE ISO").apply { setOnClickListener { chooseIso() } }, fullWidth())
        gameCard.addView(playButtonPlaceholder(), fullWidth())
        content.addView(gameCard, fullWidth())

        val cheatCard = panel()
        cheatCard.addView(sectionTitle("CHEAT RUMBLE RACING"), matchWrap())
        cheatCard.addView(text(
            "Password cheat bawaan game. Tekan Salin, lalu masukkan password di dalam Rumble Racing melalui Options > Load/Save > Password.",
            13, muted, false
        ).apply { setPadding(0, dp(8), 0, dp(10)) }, matchWrap())
        cheatStatus = text(
            "Password game (bukan kode memory patch emulator). Jika tidak diterima, periksa ejaan dan versi game.",
            12, white, false
        ).apply { setPadding(0, 0, 0, dp(10)) }
        cheatCard.addView(cheatStatus, matchWrap())
        val cheatList = listOf(
            "Buka semua trek/cup/video" to "ZEAGTLUKE",
            "Buckshot" to "UBTCKSTOH",
            "Cobalt" to "TLACOBTLA",
            "Gamecus" to "BSUIGASUM",
            "High Roller" to "HGIROLREL",
            "Interceptor" to "CDAAPTN1A",
            "Redneck Rocket" to "KCEROCTEC",
            "Revolution" to "PTOATRTO1",
            "Road Trip" to "ABOGOBOGA",
            "Sporticus" to "OPSRTISUC",
            "Stinger" to "AMHBRAAMH",
            "Thor" to "THTORHROT",
            "Van Itty" to "VTYANIYTT",
            "Vortex" to "1AREXT1AR",
            "XXS Tomcat" to "NALDSHHSD",
            "Pro Cup 2 + Falls Down + Cataclysm" to "P1PROC1PU",
            "Pro Cup 3 + The Gauntlet + EsCargot" to "Q2PROC2YT",
            "Elite Cup + Surf & Turf + Road Kill" to "AEPPROPUC",
            "Elite Cup 2 + Coal Cuts + Jolly Roger" to "ILETEC1MB",
            "Elite Cup 3 + Wild Kingdom + Malice" to "ILCTEC2VB",
            "Elite Cup 4 + Over Easy + Direwolf" to "ILQTEC3PU",
            "EA Elite Cup + Outer Limits + Blue Devil" to "LEAITEPUC",
            "EA Stunt Cup + Grand Champion video" to "YEAMPLOWW",
            "Championship mode 1" to "KOZIEC1PU",
            "Championship mode 2" to "KZOIEC2P1",
            "Championship mode 3" to "OORKIEPUC"
        )
        cheatList.forEach { (effect, password) ->
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(0, dp(5), 0, dp(5))
            }
            val label = text("$effect\n$password", 12, white, false).apply {
                setPadding(0, 0, dp(8), 0)
            }
            row.addView(label, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
            row.addView(button("SALIN").apply {
                textSize = 11f
                minHeight = dp(40)
                setOnClickListener {
                    val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("Rumble Racing password", password))
                    Toast.makeText(this@MainActivity, "Password $password disalin", Toast.LENGTH_SHORT).show()
                }
            }, LinearLayout.LayoutParams(dp(88), ViewGroup.LayoutParams.WRAP_CONTENT))
            cheatCard.addView(row, matchWrap())
        }
        content.addView(cheatCard, fullWidth())

        val graphicsCard = panel()
        graphicsCard.addView(sectionTitle("GRAFIK & PERFORMA"), matchWrap())
        graphicsCard.addView(text(
            "Pilihan berikut menyimpan preferensi di aplikasi. Pada versi ini belum mengubah renderer emulator secara langsung.",
            12, muted, false
        ).apply { setPadding(0, dp(6), 0, dp(10)) }, matchWrap())

        graphicsCard.addView(text("Preset", 14, white, true), matchWrap())
        val presetSpinner = Spinner(this).apply {
            adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item,
                listOf("HP kentang • paling ringan", "Seimbang", "Grafik lebih tajam"))
            setSelection(prefs.getInt("preset", 0).coerceIn(0, 2))
            onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                    prefs.edit().putInt("preset", position).apply()
                    updateSettingsSummary()
                }
                override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
            }
        }
        graphicsCard.addView(presetSpinner, fullWidth())

        graphicsCard.addView(text("Resolusi internal (preferensi)", 14, white, true).apply {
            setPadding(0, dp(10), 0, 0)
        }, matchWrap())
        val resolutionSpinner = Spinner(this).apply {
            adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item,
                listOf("50% • sangat ringan", "75% • rekomendasi awal", "100% • native", "150% • tajam, berat"))
            setSelection(prefs.getInt("resolution", 1).coerceIn(0, 3))
            onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                    prefs.edit().putInt("resolution", position).apply()
                    updateSettingsSummary()
                }
                override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
            }
        }
        graphicsCard.addView(resolutionSpinner, fullWidth())

        graphicsCard.addView(text("Target FPS (preferensi)", 14, white, true).apply {
            setPadding(0, dp(10), 0, 0)
        }, matchWrap())
        val fpsSpinner = Spinner(this).apply {
            adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item,
                listOf("30 FPS", "50 FPS", "60 FPS"))
            setSelection(prefs.getInt("fps", 0).coerceIn(0, 2))
            onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                    prefs.edit().putInt("fps", position).apply()
                    updateSettingsSummary()
                }
                override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
            }
        }
        graphicsCard.addView(fpsSpinner, fullWidth())

        settingsStatus = text("", 12, cyan, false).apply { setPadding(0, dp(12), 0, 0) }
        graphicsCard.addView(settingsStatus, matchWrap())
        content.addView(graphicsCard, fullWidth())

        val extraCard = panel()
        extraCard.addView(sectionTitle("FITUR TAMBAHAN"), matchWrap())
        extraCard.addView(text(
            "• Tombol reset pilihan ISO\n• Catatan konfigurasi\n• Statistik FPS aktual dan kontrol overlay direncanakan untuk integrasi emulator berikutnya.",
            13, muted, false
        ).apply { setPadding(0, dp(8), 0, dp(10)) }, matchWrap())
        extraCard.addView(button("LUPAKAN FILE ISO").apply {
            setOnClickListener {
                prefs.edit().remove("iso_uri").apply()
                isoUri = null
                refreshIsoStatus()
                Toast.makeText(this@MainActivity, "Lokasi ISO dihapus.", Toast.LENGTH_SHORT).show()
            }
        }, fullWidth())
        content.addView(extraCard, fullWidth())

        content.addView(text(
            "Catatan: cheat, FPS, resolusi, dan opsi grafik di versi ini adalah antarmuka preferensi, belum mengubah mesin emulator. Integrasi nyata memerlukan API/konfigurasi yang didukung emulator PS2 terpasang. Gunakan ISO dan BIOS yang diperoleh secara legal.",
            11, muted, false
        ).apply {
            gravity = Gravity.CENTER
            setPadding(dp(4), dp(16), dp(4), dp(4))
        }, matchWrap())

        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f
        ))
        setContentView(root)
        updateSettingsSummary()
    }

    private fun playButtonPlaceholder(): Button {
        playButton = button("MAIN SEKARANG").apply {
            setOnClickListener { launchEmulator() }
            isEnabled = false
            alpha = 0.5f
        }
        return playButton
    }

    private fun panel() = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(16), dp(16), dp(16), dp(16))
        setBackgroundColor(panel)
    }

    private fun sectionTitle(s: String) = text(s, 12, cyan, true)

    private fun chooseIso() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        startActivityForResult(intent, pickIsoRequest)
    }

    @Deprecated("Activity result API kept minimal for starter project")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != pickIsoRequest || resultCode != RESULT_OK) return
        val uri = data?.data ?: return
        val flags = data.flags and Intent.FLAG_GRANT_READ_URI_PERMISSION
        try { contentResolver.takePersistableUriPermission(uri, flags) } catch (_: Exception) {}
        isoUri = uri
        prefs.edit().putString("iso_uri", uri.toString()).apply()
        refreshIsoStatus()
    }

    private fun refreshIsoStatus() {
        val uri = isoUri
        if (uri == null) {
            isoStatus.text = "Belum ada ISO dipilih. Pilih file Rumble Racing (.iso) dari penyimpanan."
            playButton.isEnabled = false
            playButton.alpha = 0.5f
            return
        }
        val name = queryDisplayName(uri) ?: uri.lastPathSegment ?: "File ISO tersimpan"
        isoStatus.text = "ISO dipilih:\n$name"
        playButton.isEnabled = true
        playButton.alpha = 1f
    }

    private fun launchEmulator() {
        val uri = isoUri ?: return
        for (mime in listOf("application/x-iso9660-image", "application/octet-stream", "*/*")) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, mime)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            try {
                startActivity(Intent.createChooser(intent, "Buka Rumble Racing dengan emulator PS2"))
                return
            } catch (_: ActivityNotFoundException) {
            } catch (_: Exception) {
            }
        }
        Toast.makeText(this, "Tidak ada aplikasi yang menerima file ISO. Buka emulator PS2 dan pilih ISO dari sana.", Toast.LENGTH_LONG).show()
    }

    private fun updateSettingsSummary() {
        if (!::settingsStatus.isInitialized) return
        val preset = listOf("HP kentang", "Seimbang", "Grafik tajam")[prefs.getInt("preset", 0).coerceIn(0, 2)]
        val resolution = listOf("50%", "75%", "100%", "150%")[prefs.getInt("resolution", 1).coerceIn(0, 3)]
        val fps = listOf("30", "50", "60")[prefs.getInt("fps", 0).coerceIn(0, 2)]
        settingsStatus.text = "Tersimpan: $preset • resolusi $resolution • target $fps FPS\nBelum diterapkan ke emulator sampai integrasi backend tersedia."
    }

    private fun queryDisplayName(uri: Uri): String? = try {
        contentResolver.query(uri, arrayOf("_display_name"), null, null, null)?.use {
            if (it.moveToFirst()) it.getString(0) else null
        }
    } catch (_: Exception) { null }

    private fun text(value: String, sizeSp: Int, color: Int, bold: Boolean) =
        TextView(this).apply {
            text = value
            textSize = sizeSp.toFloat()
            setTextColor(color)
            if (bold) setTypeface(typeface, Typeface.BOLD)
        }

    private fun button(label: String) = Button(this).apply {
        text = label
        textSize = 14f
        isAllCaps = false
        setTextColor(white)
        setBackgroundColor(Color.rgb(0, 145, 210))
        minHeight = dp(50)
        setPadding(dp(10), dp(7), dp(10), dp(7))
    }

    private fun matchWrap() = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    )

    private fun fullWidth() = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply { bottomMargin = dp(10) }

    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()
}


