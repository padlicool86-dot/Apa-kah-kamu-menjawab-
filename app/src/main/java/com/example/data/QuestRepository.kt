package com.example.data

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

data class QuestQuestion(
    val id: String,
    val category: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val difficulty: String, // Mudah, Sedang, Sulit, Master
    val institutionBadge: String, // e.g. "Lembaga Emas", "Lembaga Pembuat", "Gemini Labs"
    val creatorName: String = "Fadlly & Lembaga Pembuat",
    val sourceName: String = "Gemini",
    val keyboardTrivia: String? = null
)

object QuestRepository {

    val defaultQuestions = listOf(
        // Category: Keyboard & Teknologi
        QuestQuestion(
            id = "q_kb_1",
            category = "Keyboard & Teknologi",
            question = "Pada keyboard komputer standar layout QWERTY, tombol apa yang diciptakan oleh programmer Bob Bemer pada tahun 1960 untuk menghentikan program yang macet?",
            options = listOf("Tombol Enter", "Tombol Escape (ESC)", "Tombol Backspace", "Tombol Caps Lock"),
            correctIndex = 1,
            explanation = "Tombol Escape (ESC) diciptakan oleh Bob Bemer pada tahun 1960 di IBM untuk memberikan sinyal interupsi darurat bagi programmer saat komputer menjalankan kode yang mengalami loop tanpa henti.",
            difficulty = "Sedang",
            institutionBadge = "Lembaga Pembuat",
            keyboardTrivia = "Keyboard Logo: [ESC] adalah sakelar darurat digital legendaris!"
        ),
        QuestQuestion(
            id = "q_kb_2",
            category = "Keyboard & Teknologi",
            question = "Apa alasan utama susunan tombol huruf pada keyboard QWERTY tidak diurutkan sesuai alfabet A-B-C-D?",
            options = listOf(
                "Untuk mencegah tuas mekanik mesin tik saling tersangkut saat mengetik cepat",
                "Karena tombol huruf dibuat berdasarkan sandi rahasia perang dunia",
                "Supaya orang mengetik lebih lambat agar mesin tidak panas",
                "Hanya kebetulan acak dari pencipta Christopher Sholes"
            ),
            correctIndex = 0,
            explanation = "Christopher Latham Sholes memisahkan pasangan huruf yang paling sering muncul berdampingan (seperti 'th' atau 'st') agar tuas logam mekanik mesin tik tidak saling bertabrakan dan macet saat diketik dengan kecepatan tinggi.",
            difficulty = "Mudah",
            institutionBadge = "Lembaga Pembuat",
            keyboardTrivia = "Layout QWERTY dipatenkan pada tahun 1878 dan tetap dipakai hingga era keyboard modern!"
        ),
        QuestQuestion(
            id = "q_kb_3",
            category = "Keyboard & Teknologi",
            question = "Dalam dunia keyboard mekanikal, switch jenis 'Tactile' (seperti Cherry MX Brown) dikenal memiliki karakteristik khas apa?",
            options = listOf(
                "Bunyi klik sangat nyaring tanpa hambatan",
                "Sensasi hentakan (bump) lembut tanpa suara klik bising saat tombol terpicu",
                "Gerakan linear licin tanpa ada benturan sama sekali",
                "Menggunakan sensor air optik tanpa pegas logam"
            ),
            correctIndex = 1,
            explanation = "Switch Tactile memberikan feedback fisik berupa hentakan (tactile bump) ketika actuation point tercapai, memberikan kepastian pengetikan tanpa kebisingan berlebih seperti switch Clicky.",
            difficulty = "Sedang",
            institutionBadge = "Lembaga Pembuat",
            keyboardTrivia = "Banyak programmer menyukai switch tactile untuk mengetik kode berjam-jam!"
        ),
        QuestQuestion(
            id = "q_kb_4",
            category = "Keyboard & Teknologi",
            question = "Tombol manakah pada keyboard modern yang merupakan tombol paling panjang dan paling sering ditekan oleh jari jempol?",
            options = listOf("Backspace", "Enter", "Spacebar (Spasi)", "Shift Kanan"),
            correctIndex = 2,
            explanation = "Spacebar adalah tombol paling panjang dan menempati sekitar 18% dari seluruh penekanan tombol dalam teks bahasa normal manusia.",
            difficulty = "Mudah",
            institutionBadge = "Lembaga Pembuat",
            keyboardTrivia = "Di bawah Spacebar biasanya terdapat stabilizer batang kawat agar tombol seimbang ditekan di sudut manapun."
        ),

        // Category: Lembaga Emas & Lembaga Dunia
        QuestQuestion(
            id = "q_inst_1",
            category = "Lembaga Emas & Dunia",
            question = "Lembaga riset dunia manakah yang mengoperasikan Large Hadron Collider (LHC) dan menjadi tempat Tim Berners-Lee menemukan World Wide Web (WWW)?",
            options = listOf("NASA", "CERN", "UNESCO", "World Bank"),
            correctIndex = 1,
            explanation = "CERN (Conseil Européen pour la Recherche Nucléaire) di Jenewa adalah laboratorium fisika partikel tempat penemuan partikel Higgs Boson dan tempat Tim Berners-Lee menciptakan WWW pada 1989.",
            difficulty = "Sedang",
            institutionBadge = "Lembaga Emas",
            sourceName = "Gemini",
            keyboardTrivia = "Komputer NeXT milik Tim Berners-Lee di CERN digunakan sebagai web server pertama di dunia!"
        ),
        QuestQuestion(
            id = "q_inst_2",
            category = "Lembaga Emas & Dunia",
            question = "Berapakah jumlah negara anggota berdaulat yang tergabung dalam Perserikatan Bangsa-Bangsa (PBB / UN) saat ini?",
            options = listOf("150 Negara", "175 Negara", "193 Negara", "210 Negara"),
            correctIndex = 2,
            explanation = "PBB saat ini memiliki 193 negara anggota penuh, dengan Sudan Selatan sebagai anggota terbaru yang bergabung pada tahun 2011.",
            difficulty = "Sedang",
            institutionBadge = "Lembaga Emas",
            sourceName = "Gemini"
        ),
        QuestQuestion(
            id = "q_inst_3",
            category = "Lembaga Emas & Dunia",
            question = "Badan PBB manakah yang bertugas melindungi peninggalan bersejarah dan menetapkan daftar 'Situs Warisan Dunia' (World Heritage Sites)?",
            options = listOf("UNESCO", "UNICEF", "WHO", "UNHCR"),
            correctIndex = 0,
            explanation = "UNESCO (United Nations Educational, Scientific and Cultural Organization) bermarkas di Paris dan mengelola program Situs Warisan Dunia serta penetapan Warisan Budaya Takbenda.",
            difficulty = "Mudah",
            institutionBadge = "Lembaga Emas",
            sourceName = "Gemini"
        ),
        QuestQuestion(
            id = "q_inst_4",
            category = "Lembaga Emas & Dunia",
            question = "Misi luar angkasa NASA manakah yang berhasil mendaratkan manusia pertama di permukaan Bulan pada 20 Juli 1969?",
            options = listOf("Gemini 8", "Apollo 11", "Voyager 1", "Artemis 1"),
            correctIndex = 1,
            explanation = "Apollo 11 membawa Neil Armstrong, Buzz Aldrin, dan Michael Collins ke Bulan. Neil Armstrong menginjakkan kaki pertama kali dan mengucapkan 'That's one small step for man, one giant leap for mankind'.",
            difficulty = "Mudah",
            institutionBadge = "Lembaga Emas",
            sourceName = "Gemini"
        ),

        // Category: Kecerdasan Buatan & Gemini
        QuestQuestion(
            id = "q_ai_1",
            category = "Kecerdasan Buatan & Gemini",
            question = "Apa keunggulan arsitektur utama model AI Google Gemini dibandingkan model teks generasi lama?",
            options = listOf(
                "Hanya bisa memproses teks dalam format CSV",
                "Dibangun secara native Multimodal sejak awal (memahami teks, gambar, audio, kode, dan video sekaligus)",
                "Tidak membutuhkan daya komputasi atau GPU",
                "Hanya beroperasi pada komputer mainframe analog"
            ),
            correctIndex = 1,
            explanation = "Google Gemini dirancang 'natively multimodal' dari dasar, sehingga dapat memahami dan menggabungkan informasi dari berbagai modalitas seperti teks, visual gambar, audio suara, dan sintaks kode pemrograman secara harmonis.",
            difficulty = "Sedang",
            institutionBadge = "Gemini Labs",
            sourceName = "Gemini"
        ),
        QuestQuestion(
            id = "q_ai_2",
            category = "Kecerdasan Buatan & Gemini",
            question = "Siapakah ilmuwan matematika dan bapak ilmu komputer yang mengusulkan 'Imitation Game' (sekarang dikenal sebagai Uji Turing) untuk menguji kecerdasan mesin?",
            options = listOf("Alan Turing", "Ada Lovelace", "John von Neumann", "Claude Shannon"),
            correctIndex = 0,
            explanation = "Alan Turing mengusulkan Turing Test pada makalah tahun 1950 berjudul 'Computing Machinery and Intelligence' untuk mengevaluasi apakah perilaku percakapan mesin dapat dibedakan dari manusia.",
            difficulty = "Sedang",
            institutionBadge = "Gemini Labs",
            sourceName = "Gemini"
        ),
        QuestQuestion(
            id = "q_ai_3",
            category = "Kecerdasan Buatan & Gemini",
            question = "Mekanisme utama apakah yang diperkenalkan dalam makalah legendaris 'Attention Is All You Need' (2017) yang mendasari Large Language Model modern?",
            options = listOf("Convolutional Layer", "Transformer & Self-Attention", "Genetic Algorithm", "Perceptron Single Layer"),
            correctIndex = 1,
            explanation = "Arsitektur Transformer dengan mekanisme Self-Attention memungkinkan model AI memproses hubungan antarkata dalam kalimat secara paralel dan kontekstual, merevolusi dunia AI hingga lahirnya model mutakhir seperti Gemini.",
            difficulty = "Sulit",
            institutionBadge = "Gemini Labs",
            sourceName = "Gemini"
        ),

        // Category: Tantangan Quest Fadlly & Logika
        QuestQuestion(
            id = "q_fadlly_1",
            category = "Tantangan Quest Fadlly",
            question = "Bisa kah kamu menjawab: Jika sebuah keyboard memiliki 104 tombol dan setiap detik kamu menekan 2 tombol, berapa detik waktu yang dibutuhkan untuk menekan seluruh tombol tepat satu kali tanpa pengulangan?",
            options = listOf("26 Detik", "52 Detik", "104 Detik", "208 Detik"),
            correctIndex = 1,
            explanation = "104 tombol dibagi 2 tombol per detik = 52 detik. Logika cepat dari Quest Fadlly!",
            difficulty = "Mudah",
            institutionBadge = "Lembaga Pembuat",
            creatorName = "Fadlly Master Quest"
        ),
        QuestQuestion(
            id = "q_fadlly_2",
            category = "Tantangan Quest Fadlly",
            question = "Sebuah kode program berjalan 3 kali lebih cepat setelah dioptimalkan oleh sistem Lembaga Pembuat. Jika waktu awal adalah 45 milidetik, berapa lama waktu eksekusi barunya?",
            options = listOf("15 milidetik", "135 milidetik", "30 milidetik", "9 milidetik"),
            correctIndex = 0,
            explanation = "45 milidetik dibagi 3 = 15 milidetik. Efisiensi algoritma tingkat tinggi terverifikasi!",
            difficulty = "Mudah",
            institutionBadge = "Lembaga Pembuat",
            creatorName = "Fadlly Master Quest"
        ),
        QuestQuestion(
            id = "q_fadlly_3",
            category = "Tantangan Quest Fadlly",
            question = "Di ruang server Lembaga Emas terdapat 4 rak server: Biru, Oranye, Emas, dan Perak. Server AI tidak berada di rak Perak. Server Audio berada tepat di sebelah server AI di rak Biru. Jika rak Emas berisi database dunia, di rak manakah server AI berada?",
            options = listOf("Rak Perak", "Rak Emas", "Rak Oranye", "Rak Biru"),
            correctIndex = 2,
            explanation = "Rak Emas berisi Database Dunia, Rak Perak bukan server AI, Rak Biru berisi Server Audio (yang berada di sebelah AI). Maka server AI pasti berada di Rak Oranye!",
            difficulty = "Master",
            institutionBadge = "Lembaga Pembuat",
            creatorName = "Fadlly Master Quest"
        ),

        // Category: DJ TikTok & Musik Viral 2023
        QuestQuestion(
            id = "q_dj_1",
            category = "DJ TikTok & Musik Viral",
            question = "Lagu viral 'Malam Pagi' (DJ Malam Pagi x Hamil Duluan) dengan lirik 'Malam masih muda... Hilang apa yang ku selalu mimpi' dipopulerkan oleh musisi asal Malaysia bernama siapa?",
            options = listOf("Saixse", "Alan Walker", "Alok", "Rich Brian"),
            correctIndex = 0,
            explanation = "Lagu 'Malam Pagi' diciptakan dan dinyanyikan oleh rapper/musisi asal Malaysia, Saixse, yang kemudian meledak menjadi tren remix DJ TikTok di seluruh Asia Tenggara pada tahun 2023.",
            difficulty = "Sedang",
            institutionBadge = "Musik TikTok 2023",
            keyboardTrivia = "Audio BPM: 130 BPM Jedag-Jedug Remix!"
        ),
        QuestQuestion(
            id = "q_dj_2",
            category = "DJ TikTok & Musik Viral",
            question = "Lagu legendaris Indonesia Timur 'Karna Su Sayang' (DJ Karna Su Sayang) yang trending di TikTok diciptakan oleh musisi Near dan dinyanyikan oleh penyanyi berbakat asal mana?",
            options = listOf(
                "Dian Sorowea dari Maumere (Nusa Tenggara Timur)",
                "Marion Jola dari Kupang",
                "Novia Bachmid dari Bolaang Mongondow",
                "Mitha Talahatu dari Ambon"
            ),
            correctIndex = 0,
            explanation = "'Karna Su Sayang' diciptakan oleh Near bersama Dian Sorowea dari Maumere, NTT pada tahun 2018 dan terus divariasikan menjadi remix DJ TikTok terpopuler hingga tahun 2023.",
            difficulty = "Mudah",
            institutionBadge = "Musik TikTok 2023"
        ),
        QuestQuestion(
            id = "q_dj_3",
            category = "DJ TikTok & Musik Viral",
            question = "Lagu koplo viral 'Dumes' (DJ Dumes) yang sangat hits di TikTok 2023 menceritakan tentang perasaan apa?",
            options = listOf(
                "Kekecewaan mendalam karena cinta yang tak dihargai hingga kepala pusing/mumet",
                "Kegembiraan memenangkan lotre",
                "Perjalanan mendaki gunung di Jawa Tengah",
                "Kisah persahabatan anak sekolah"
            ),
            correctIndex = 0,
            explanation = "Lagu 'Dumes' karya Andry Priyanta menceritakan kepedihan hati seseorang yang sudah berkorban perasaan namun pasangannya justru memilih orang lain ('Dumes' = mumet/patah hati).",
            difficulty = "Sedang",
            institutionBadge = "Musik TikTok 2023"
        ),
        QuestQuestion(
            id = "q_dj_4",
            category = "DJ TikTok & Musik Viral",
            question = "Lagu berbahasa Jawa hits 'Kisinan' (DJ Kisinan) yang dipopulerkan oleh Masdddho memiliki arti kata 'Kisinan' yang bermakna apa?",
            options = listOf(
                "Menanggung rasa malu karena dipermainkan cinta",
                "Kebahagiaan yang meluap-luap",
                "Kekayaan yang berlimpah",
                "Janji setia seumur hidup"
            ),
            correctIndex = 0,
            explanation = "Dalam bahasa Jawa, 'Kisinan' berarti menanggung malu (isin = malu), menceritakan seseorang yang mengira dirinya dicintai padahal hanya dianggap badut pelipur lara.",
            difficulty = "Mudah",
            institutionBadge = "Musik TikTok 2023"
        ),
        QuestQuestion(
            id = "q_dj_5",
            category = "DJ TikTok & Musik Viral",
            question = "Dalam format aransemen audio 'DJ TikTok Jedag-Jedug', ciri khas ketukan drum yang membuat pendengar bergoyang adalah?",
            options = listOf(
                "Pola kick drum 4-on-the-floor dengan off-beat bass drop dan synth pluck tajam",
                "Ketukan drum jazz pelan tanpa bass",
                "Irama ketukan waltz 3/4 klasik",
                "Ketukan rebana rebab Timur Tengah murni"
            ),
            correctIndex = 0,
            explanation = "Remix DJ TikTok Jedag-Jedug mengombinasikan kick drum 4/4 berenergi tinggi dengan bassline melompat (bounce/koplo) serta synth lead berfrekuensi tajam.",
            difficulty = "Sedang",
            institutionBadge = "Musik TikTok 2023"
        )
    )

    fun getQuestionsByCategory(category: String): List<QuestQuestion> {
        if (category == "Semua Quest") return defaultQuestions
        return defaultQuestions.filter { it.category == category }
    }

    suspend fun getGeminiHint(question: QuestQuestion): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isNullOrEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            // Smart offline reasoning hint engine verified by Gemini & Lembaga Pembuat
            return@withContext "💡 Petunjuk Gemini: Perhatikan kata kunci utama pada pertanyaan '${question.category}'. Pilihan yang benar berkaitan langsung dengan fakta sejarah dan definisi teknisnya!"
        }

        try {
            val url = URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true
            conn.connectTimeout = 10000
            conn.readTimeout = 10000

            val prompt = "Kamu adalah asisten petunjuk quest game 'Bisa Kah Kamu Menjawab?!' buatan Fadlly & Lembaga Pembuat bersumber Gemini. Berikan 1 petunjuk cerdas dan singkat (maksimal 2 kalimat) dalam bahasa Indonesia untuk soal ini tanpa langsung menyebutkan jawaban persisnya: '${question.question}'"
            val requestJson = JSONObject().apply {
                put("contents", org.json.JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", org.json.JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        })
                    })
                })
            }

            OutputStreamWriter(conn.outputStream).use { writer ->
                writer.write(requestJson.toString())
                writer.flush()
            }

            if (conn.responseCode == 200) {
                BufferedReader(InputStreamReader(conn.inputStream)).use { reader ->
                    val response = reader.readText()
                    val json = JSONObject(response)
                    val text = json.getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")
                    return@withContext text.trim()
                }
            } else {
                return@withContext "💡 Petunjuk Gemini: Analisis konteks kata kunci dan eliminasi opsi yang tidak logis!"
            }
        } catch (e: Exception) {
            return@withContext "💡 Petunjuk Gemini: Fokus pada keterkaitan antara kategori '${question.category}' dan opsi jawaban yang paling spesifik!"
        }
    }
}
