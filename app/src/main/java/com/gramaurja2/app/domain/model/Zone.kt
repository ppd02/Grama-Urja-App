package com.gramaurja2.app.domain.model

data class Zone(
    val id: String,
    val nameEn: String,
    val nameKn: String,
    val district: String,
    val transformer: String,
    val feeder: String
) {
    fun displayName(language: Language): String = if (language == Language.Kannada) nameKn else nameEn
    fun topic(): String = "zone_${id.replace('-', '_')}"
}

object ZoneCatalog {
    val zones = listOf(
        Zone("hubli-unkal-t1", "Hubli - Unkal Feeder", "ಹುಬ್ಬಳ್ಳಿ - ಉಣಕಲ್ ಫೀಡರ್", "Dharwad", "T1", "North Smart Feeder"),
        Zone("mandya-sugar-t2", "Mandya - Sugar Belt", "ಮಂಡ್ಯ - ಕಬ್ಬು ವಲಯ", "Mandya", "T2", "Kaveri Rural Feeder"),
        Zone("mysuru-varuna-t3", "Mysuru - Varuna Line", "ಮೈಸೂರು - ವರುಣ ಲೈನ್", "Mysuru", "T3", "Varuna Solar Assist"),
        Zone("belagavi-gokak-t4", "Belagavi - Gokak Cluster", "ಬೆಳಗಾವಿ - ಗೋಕಾಕ ಕ್ಲಸ್ಟರ್", "Belagavi", "T4", "Ghataprabha Feeder"),
        Zone("raichur-sindhanur-t5", "Raichur - Sindhanur Pump Belt", "ರಾಯಚೂರು - ಸಿಂಧನೂರು ಪಂಪ್ ವಲಯ", "Raichur", "T5", "Tungabhadra Feeder"),
        Zone("hassan-arsikere-t6", "Hassan - Arsikere Line", "ಹಾಸನ - ಅರಸೀಕೆರೆ ಲೈನ್", "Hassan", "T6", "Hemavathi Feeder"),
        Zone("shivamogga-tunga-t7", "Shivamogga - Tunga Canal", "ಶಿವಮೊಗ್ಗ - ತುಂಗಾ ಕಾಲುವೆ", "Shivamogga", "T7", "Tunga Lift Feeder"),
        Zone("kalaburagi-jeevargi-t8", "Kalaburagi - Jeevargi Dry Belt", "ಕಲಬುರಗಿ - ಜೇವರ್ಗಿ ಒಣ ವಲಯ", "Kalaburagi", "T8", "Bhima Rural Feeder")
    )

    val defaultZone: Zone = zones.first()
    fun byId(id: String?): Zone = zones.firstOrNull { it.id == id } ?: defaultZone
    fun byIds(ids: Set<String>): List<Zone> = ids.map(::byId).distinctBy { it.id }.ifEmpty { listOf(defaultZone) }
}
