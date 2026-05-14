package com.gramaurja2.app.data.local

import com.gramaurja2.app.domain.model.CropType
import com.gramaurja2.app.domain.model.Language
import com.gramaurja2.app.domain.model.PowerStatus
import com.gramaurja2.app.domain.model.PowerStatusUpdate
import com.gramaurja2.app.domain.model.Zone
import com.gramaurja2.app.domain.model.dualText
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IrrigationTipsRepository @Inject constructor() {
    fun advice(
        zone: Zone, 
        crop: CropType, 
        update: PowerStatusUpdate, 
        language: Language,
        rainPredicted: Boolean = false
    ): String {
        val weatherAdvice = if (rainPredicted) {
            dualText(
                "⚠️ SMART ALERT: Heavy rain is predicted in the next 24 hours. Suggest skipping irrigation to save water and electricity.",
                "⚠️ ಸ್ಮಾರ್ಟ್ ಅಲರ್ಟ್: ಮುಂದಿನ 24 ಗಂಟೆಗಳಲ್ಲಿ ಭಾರಿ ಮಳೆಯ ಮುನ್ಸೂಚನೆ ಇದೆ. ನೀರು ಮತ್ತು ವಿದ್ಯುತ್ ಉಳಿಸಲು ನೀರಾವರಿಯನ್ನು ಬಿಟ್ಟುಬಿಡಲು ಸೂಚಿಸಲಾಗಿದೆ.",
                language
            )
        } else null

        val powerAdvice = when (update.status) {
            PowerStatus.ON -> dualText(
                "Power is available in ${zone.nameEn}. Start irrigation now if the soil is dry, and finish the most important field first.",
                "${zone.nameKn} ನಲ್ಲಿ ವಿದ್ಯುತ್ ಇದೆ. ಮಣ್ಣು ಒಣಗಿದ್ದರೆ ಈಗ ನೀರಾವರಿ ಶುರು ಮಾಡಿ, ಮುಖ್ಯ ಹೊಲವನ್ನು ಮೊದಲು ಮುಗಿಸಿ.",
                language
            )
            PowerStatus.OFF -> dualText(
                "Power is currently off. Keep the pump starter, pipe line, and valves ready for the next supply window.",
                "ಈಗ ವಿದ್ಯುತ್ ಇಲ್ಲ. ಮುಂದಿನ ವಿದ್ಯುತ್ ಸಮಯಕ್ಕೆ ಪಂಪ್ ಸ್ಟಾರ್ಟರ್, ಪೈಪ್ ಲೈನ್ ಮತ್ತು ವಾಲ್ವ್‌ಗಳನ್ನು ಸಿದ್ಧವಾಗಿಡಿ.",
                language
            )
            PowerStatus.UNKNOWN -> dualText(
                "Power status is not confirmed. Check the starter or nearby transformer before switching on the motor.",
                "ವಿದ್ಯುತ್ ಸ್ಥಿತಿ ಖಚಿತವಿಲ್ಲ. ಮೋಟಾರ್ ಆನ್ ಮಾಡುವ ಮೊದಲು ಸ್ಟಾರ್ಟರ್ ಅಥವಾ ಹತ್ತಿರದ ಟ್ರಾನ್ಸ್‌ಫಾರ್ಮರ್ ಪರಿಶೀಲಿಸಿ.",
                language
            )
        }

        val cropAdvice = when (crop.id) {
            "paddy" -> dualText(
                "For paddy, maintain shallow water. Avoid continuous overflow; short cycles save water and protect nutrients.",
                "ಭತ್ತಕ್ಕೆ ಕಡಿಮೆ ಆಳದ ನೀರು ಸಾಕು. ನಿರಂತರವಾಗಿ ನೀರು ಹರಿಯದಂತೆ ನೋಡಿ; ಚಿಕ್ಕ ಚಕ್ರಗಳು ನೀರು ಮತ್ತು ಪೋಷಕಾಂಶ ಉಳಿಸುತ್ತವೆ.",
                language
            )
            "sugarcane" -> dualText(
                "For sugarcane, irrigate deeply but less often. Give priority during active growth and avoid midday watering.",
                "ಕಬ್ಬಿಗೆ ಆಳವಾಗಿ ಆದರೆ ಕಡಿಮೆ ಬಾರಿ ನೀರು ನೀಡಿ. ಬೆಳವಣಿಗೆ ಹಂತದಲ್ಲಿ ಆದ್ಯತೆ ನೀಡಿ, ಮಧ್ಯಾಹ್ನ ನೀರಾವರಿ ತಪ್ಪಿಸಿ.",
                language
            )
            "vegetables" -> dualText(
                "For vegetables, water near the root zone in the morning or evening. Keep moisture steady, not flooded.",
                "ತರಕಾರಿಗಳಿಗೆ ಬೆಳಗ್ಗೆ ಅಥವಾ ಸಂಜೆ ಬೇರು ಭಾಗಕ್ಕೆ ನೀರು ನೀಡಿ. ತೇವಾಂಶ ಸ್ಥಿರವಾಗಿರಲಿ, ನೀರು ನಿಲ್ಲದಿರಲಿ.",
                language
            )
            "groundnut" -> dualText(
                "For groundnut, keep soil moist during flowering and pod formation. Avoid excess water near harvest.",
                "ಕಡಲೆಕಾಯಿಗೆ ಹೂವು ಮತ್ತು ಕಾಯಿ ಹಂತದಲ್ಲಿ ಮಣ್ಣು ತೇವವಾಗಿರಲಿ. ಕೊಯ್ಲು ಹತ್ತಿರ ಹೆಚ್ಚು ನೀರು ಬೇಡ.",
                language
            )
            "ragi" -> dualText(
                "For ragi, light irrigation is usually enough. Avoid over-watering after the crop is established.",
                "ರಾಗಿಗೆ ಸಾಮಾನ್ಯವಾಗಿ ಕಡಿಮೆ ನೀರಾವರಿ ಸಾಕು. ಬೆಳೆ ಹಿಡಿದ ನಂತರ ಹೆಚ್ಚು ನೀರು ಕೊಡಬೇಡಿ.",
                language
            )
            "arecanut" -> dualText(
                "For arecanut, use controlled basin irrigation. Do not allow standing water around roots.",
                "ಅಡಿಕೆಗೆ ನಿಯಂತ್ರಿತ ಬೇಸಿನ್ ನೀರಾವರಿ ಮಾಡಿ. ಬೇರು ಬಳಿ ನೀರು ನಿಲ್ಲದಂತೆ ನೋಡಿ.",
                language
            )
            "maize" -> dualText(
                "For maize, prioritize watering before tasseling and grain filling. Avoid water stress in these stages.",
                "ಮೆಕ್ಕೆಜೋಳಕ್ಕೆ ಪುಷ್ಪಿಸುವ ಮೊದಲು ಮತ್ತು ಕಾಳು ತುಂಬುವಾಗ ನೀರು ಮುಖ್ಯ. ಈ ಹಂತಗಳಲ್ಲಿ ನೀರಿನ ಕೊರತೆ ತಪ್ಪಿಸಿ.",
                language
            )
            else -> dualText(
                "For cotton, irrigate moderately and reduce watering when bolls begin opening.",
                "ಹತ್ತಿಗೆ ಮಧ್ಯಮ ನೀರಾವರಿ ಮಾಡಿ; ಕಾಯಿ ತೆರೆದುಕೊಳ್ಳುವಾಗ ನೀರು ಕಡಿಮೆ ಮಾಡಿ.",
                language
            )
        }

        val zoneAdvice = dualText(
            "Zone note: ${zone.feeder}, ${zone.district}. Coordinate with nearby farmers before long pump runs.",
            "ವಲಯ ಮಾಹಿತಿ: ${zone.feeder}, ${zone.district}. ದೀರ್ಘ ಪಂಪ್ ಚಾಲನೆಗೆ ಮುನ್ನ ಹತ್ತಿರದ ರೈತರೊಂದಿಗೆ ಸಮಯ ಹೊಂದಿಸಿ.",
            language
        )

        val savingTip = dualText(
            "Water-saving tip: check soil moisture by hand first; skip irrigation if the root zone is already wet.",
            "ನೀರು ಉಳಿಸುವ ಸಲಹೆ: ಮೊದಲು ಕೈಯಿಂದ ಮಣ್ಣಿನ ತೇವಾಂಶ ನೋಡಿ; ಬೇರು ಭಾಗ ಈಗಾಗಲೇ ತೇವವಾಗಿದ್ದರೆ ನೀರಾವರಿ ಬೇಡ.",
            language
        )

        return listOfNotNull(weatherAdvice, powerAdvice, cropAdvice, zoneAdvice, savingTip).joinToString("\n\n")
    }
}
