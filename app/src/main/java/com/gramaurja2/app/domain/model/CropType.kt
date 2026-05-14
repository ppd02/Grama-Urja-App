package com.gramaurja2.app.domain.model

data class CropType(
    val id: String,
    val nameEn: String,
    val nameKn: String,
    val baseMinutesPerAcre: Int,
    val waterNoteEn: String,
    val waterNoteKn: String
) {
    fun displayName(language: Language): String = if (language == Language.Kannada) nameKn else nameEn
    fun note(language: Language): String = if (language == Language.Kannada) waterNoteKn else waterNoteEn
}

object CropCatalog {
    val crops = listOf(
        CropType("paddy", "Paddy", "ಭತ್ತ", 210, "Keep standing water shallow and avoid overflow.", "ನೀರು ಆಳ ಕಡಿಮೆ ಇರಲಿ, ಹೆಚ್ಚುವರಿ ನೀರು ತಪ್ಪಿಸಿ."),
        CropType("sugarcane", "Sugarcane", "ಕಬ್ಬು", 240, "Use deep irrigation during active growth windows.", "ಬೆಳವಣಿಗೆ ಹಂತದಲ್ಲಿ ಆಳವಾದ ನೀರಾವರಿ ನೀಡಿ."),
        CropType("vegetables", "Vegetables", "ತರಕಾರಿಗಳು", 120, "Prefer morning or evening watering near the root zone.", "ಬೆಳಗ್ಗೆ ಅಥವಾ ಸಂಜೆ ಬೇರು ಭಾಗಕ್ಕೆ ನೀರು ನೀಡಿ."),
        CropType("groundnut", "Groundnut", "ಕಡಲೆಕಾಯಿ", 150, "Maintain steady moisture during flowering and pod formation.", "ಹೂವು ಮತ್ತು ಕಾಯಿ ಹಂತದಲ್ಲಿ ತೇವಾಂಶ ಸ್ಥಿರವಾಗಿರಲಿ."),
        CropType("ragi", "Ragi", "ರಾಗಿ", 95, "Light irrigation is usually enough after establishment.", "ಬೆಳೆ ಹಿಡಿದ ನಂತರ ಕಡಿಮೆ ನೀರಾವರಿ ಸಾಕಾಗುತ್ತದೆ."),
        CropType("arecanut", "Arecanut", "ಅಡಿಕೆ", 180, "Use controlled basin irrigation and prevent root waterlogging.", "ಬೇಸಿನ್ ನೀರಾವರಿ ನಿಯಂತ್ರಿಸಿ, ಬೇರು ಬಳಿ ನೀರು ನಿಲ್ಲದಿರಲಿ."),
        CropType("maize", "Maize", "ಮೆಕ್ಕೆಜೋಳ", 135, "Prioritize irrigation before tasseling and grain filling.", "ಪುಷ್ಪಿಸುವ ಮೊದಲು ಮತ್ತು ಕಾಳು ತುಂಬುವಾಗ ನೀರು ನೀಡಿ."),
        CropType("cotton", "Cotton", "ಹತ್ತಿ", 160, "Avoid excess watering during boll opening.", "ಕಾಯಿ ತೆರೆದುಕೊಳ್ಳುವಾಗ ಹೆಚ್ಚು ನೀರು ಬೇಡ.")
    )

    val pumpPresets = crops.take(4)
    val defaultCrop = crops.first()
    fun byId(id: String?): CropType = crops.firstOrNull { it.id == id } ?: defaultCrop
}
