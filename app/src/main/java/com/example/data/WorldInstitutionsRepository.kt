package com.example.data

data class WorldInstitution(
    val id: String,
    val name: String,
    val shortName: String,
    val category: String,
    val headquarter: String,
    val foundedYear: Int,
    val description: String,
    val goldenBadgeTitle: String,
    val memberCountOrReach: String,
    val keyAchievements: List<String>,
    val triviaFacts: List<String>
)

object WorldInstitutionsRepository {
    val institutions = listOf(
        WorldInstitution(
            id = "inst_pbb",
            name = "Perserikatan Bangsa-Bangsa (United Nations)",
            shortName = "PBB / UN",
            category = "Diplomasi & Kemanusiaan",
            headquarter = "New York, Amerika Serikat",
            foundedYear = 1945,
            description = "Lembaga internasional terbesar dunia yang beranggotakan hampir seluruh negara berdaulat di muka bumi untuk menjaga perdamaian dan hak asasi manusia.",
            goldenBadgeTitle = "Lembaga Perdamaian Tertinggi",
            memberCountOrReach = "193 Negara Anggota",
            keyAchievements = listOf(
                "Deklarasi Universal Hak Asasi Manusia 1948",
                "Operasi Penjaga Perdamaian (Peacekeeping Missions)",
                "Tujuan Pembangunan Berkelanjutan (SDGs 2030)"
            ),
            triviaFacts = listOf(
                "Piagam PBB ditandatangani di San Francisco pada 26 Juni 1945.",
                "Memiliki 6 bahasa resmi: Arab, Mandarin, Inggris, Prancis, Rusia, dan Spanyol."
            )
        ),
        WorldInstitution(
            id = "inst_nasa",
            name = "National Aeronautics and Space Administration",
            shortName = "NASA",
            category = "Sains & Antariksa",
            headquarter = "Washington, D.C., Amerika Serikat",
            foundedYear = 1958,
            description = "Badan independen pemerintah AS yang bertanggung jawab atas program luar angkasa sipil, penelitian aeronautika, dan eksplorasi kosmik.",
            goldenBadgeTitle = "Pelopor Penjelajahan Kosmos",
            memberCountOrReach = "Pusat Penelitian Global",
            keyAchievements = listOf(
                "Pendaratan Manusia Pertama di Bulan (Apollo 11, 1969)",
                "Teleskop Luar Angkasa Hubble dan James Webb",
                "Penjelajahan Mars Rover (Curiosity & Perseverance)"
            ),
            triviaFacts = listOf(
                "Didirikan sebagai tanggapan atas peluncuran satelit Sputnik 1 oleh Uni Soviet.",
                "Slogan terkenalnya adalah 'For the Benefit of All'."
            )
        ),
        WorldInstitution(
            id = "inst_cern",
            name = "European Organization for Nuclear Research",
            shortName = "CERN",
            category = "Fisika & Teknologi",
            headquarter = "Jenewa, Swiss / Prancis",
            foundedYear = 1954,
            description = "Laboratorium fisika partikel terbesar di dunia yang mengoperasikan Large Hadron Collider (LHC) dan tempat lahirnya World Wide Web (WWW).",
            goldenBadgeTitle = "Episentrum Fisika Partikel Dunia",
            memberCountOrReach = "23 Negara Anggota & Ribuan Peneliti",
            keyAchievements = listOf(
                "Penemuan Partikel Higgs Boson ('Partikel Tuhan') pada tahun 2012",
                "Kelahiran World Wide Web (diciptakan oleh Tim Berners-Lee di CERN)",
                "Akselerator partikel melingkar 27 km di bawah tanah"
            ),
            triviaFacts = listOf(
                "Suhu di dalam Large Hadron Collider lebih dingin daripada luar angkasa hampa.",
                "WWW pertama kali diusulkan tahun 1989 untuk mempermudah ilmuwan berbagi data."
            )
        ),
        WorldInstitution(
            id = "inst_who",
            name = "World Health Organization",
            shortName = "WHO",
            category = "Kesehatan Global",
            headquarter = "Jenewa, Swiss",
            foundedYear = 1948,
            description = "Badan khusus PBB yang mengarahkan dan mengoordinasikan otoritas kesehatan internasional dunia, penanganan epidemi, dan sanitasi publik.",
            goldenBadgeTitle = "Benteng Kesehatan Umat Manusia",
            memberCountOrReach = "194 Negara Anggota",
            keyAchievements = listOf(
                "Eradikasi penyakit Cacar (Smallpox) secara global pada 1980",
                "Pedoman Pandemi Global dan Vaksinasi Terpadu",
                "Kampanye Eliminasi Polio Internasional"
            ),
            triviaFacts = listOf(
                "Hari Kesehatan Dunia diperingati setiap 7 April, tanggal berdirinya WHO.",
                "Memiliki jaringan respons darurat penyakit menular 24/7 di seluruh dunia."
            )
        ),
        WorldInstitution(
            id = "inst_unesco",
            name = "United Nations Educational, Scientific and Cultural Organization",
            shortName = "UNESCO",
            category = "Pendidikan & Kebudayaan",
            headquarter = "Paris, Prancis",
            foundedYear = 1945,
            description = "Badan PBB yang mempromosikan perdamaian dan keamanan dunia melalui kerja sama internasional di bidang pendidikan, ilmu pengetahuan, seni, dan warisan budaya.",
            goldenBadgeTitle = "Pelindung Warisan Peradaban",
            memberCountOrReach = "194 Negara Anggota",
            keyAchievements = listOf(
                "Program Situs Warisan Dunia (World Heritage Sites)",
                "Konservasi Candi Borobudur dan Prambanan di Indonesia",
                "Program Literasi Global dan Hak Akses Sains Terbuka"
            ),
            triviaFacts = listOf(
                "Lebih dari 1.150 situs bersejarah dan alam di seluruh dunia dilindungi UNESCO.",
                "Menetapkan Batik Indonesia sebagai Warisan Budaya Takbenda Dunia pada 2009."
            )
        ),
        WorldInstitution(
            id = "inst_world_bank",
            name = "Bank Dunia (World Bank Group)",
            shortName = "World Bank",
            category = "Ekonomi & Pembangunan",
            headquarter = "Washington, D.C., Amerika Serikat",
            foundedYear = 1944,
            description = "Lembaga keuangan internasional yang menyediakan pinjaman dan hibah kepada pemerintah negara berpenghasilan rendah dan menengah untuk proyek modal.",
            goldenBadgeTitle = "Pendorong Kemakmuran Global",
            memberCountOrReach = "189 Negara Anggota",
            keyAchievements = listOf(
                "Pemulihan Ekonomi Pasca Perang Dunia II di Eropa",
                "Pembiayaan Infrastruktur Energi Terbarukan & Air Bersih Global",
                "Target Pengentasan Kemiskinan Ekstrem Dunia"
            ),
            triviaFacts = listOf(
                "Didirikan dalam Konferensi Bretton Woods bersama IMF pada tahun 1944.",
                "Menyediakan data statistik pembangunan terbuka untuk peneliti dunia."
            )
        ),
        WorldInstitution(
            id = "inst_mit",
            name = "Massachusetts Institute of Technology",
            shortName = "MIT",
            category = "Riset & Komputasi",
            headquarter = "Cambridge, Massachusetts, AS",
            foundedYear = 1861,
            description = "Institut riset teknologi ternama dunia yang mempelopori kecerdasan buatan, komputasi modern, arsitektur cyber, dan rekayasa sains mutakhir.",
            goldenBadgeTitle = "Kawah Candradimuka Inovasi AI",
            memberCountOrReach = "Kampus Riset Elit Dunia",
            keyAchievements = listOf(
                "Kelahiran Laboratorium Kecerdasan Buatan (CSAIL)",
                "Proyek OpenCourseWare (materi kuliah terbuka gratis untuk dunia)",
                "98 peraih Hadiah Nobel terafiliasi dengan MIT"
            ),
            triviaFacts = listOf(
                "Semboyan MIT adalah 'Mens et Manus' yang artinya 'Pikiran dan Tangan'.",
                "Tempat dikembangkannya standar antarmuka grafis sistem jendela X Window."
            )
        ),
        WorldInstitution(
            id = "inst_brin",
            name = "Badan Riset dan Inovasi Nasional",
            shortName = "BRIN",
            category = "Sains & Inovasi Nusantara",
            headquarter = "Jakarta, Indonesia",
            foundedYear = 2021,
            description = "Lembaga pemerintah non-kementerian di Indonesia yang mengintegrasikan seluruh riset sains, nuklir, antariksa (LAPAN), dan teknologi nusantara.",
            goldenBadgeTitle = "Mercusuar Riset Nusantara",
            memberCountOrReach = "Indonesia & Kolaborasi Global",
            keyAchievements = listOf(
                "Integrasi Lembaga Riset Nasional (LIPI, BPPT, BATAN, LAPAN)",
                "Pusat Genomik Keanekaragaman Hayati Tropis Terbesar",
                "Pengembangan Satelit Penginderaan Jauh Indonesia"
            ),
            triviaFacts = listOf(
                "Memiliki fasilitas riset maritim ekspedisi samudera terlengkap di Asia Tenggara.",
                "Mendukung inovasi terbuka bagi peneliti dan akademisi seluruh Indonesia."
            )
        )
    )

    fun search(query: String, selectedCategory: String = "Semua"): List<WorldInstitution> {
        val q = query.trim().lowercase()
        return institutions.filter { inst ->
            val matchCategory = selectedCategory == "Semua" || inst.category.contains(selectedCategory, ignoreCase = true)
            val matchQuery = q.isEmpty() ||
                inst.name.lowercase().contains(q) ||
                inst.shortName.lowercase().contains(q) ||
                inst.description.lowercase().contains(q) ||
                inst.headquarter.lowercase().contains(q)
            matchCategory && matchQuery
        }
    }
}
