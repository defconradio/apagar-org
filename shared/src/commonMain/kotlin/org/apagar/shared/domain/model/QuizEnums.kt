package org.apagar.shared.domain.model

enum class ChildrenBracket(
    val labelEs: String,
    val modifier: Double,
    val invoiceReasonEs: String
) {
    NONE(
        labelEs = "0 hijos",
        modifier = 0.20,
        invoiceReasonEs = "Multa por falta de relevo generacional (0 futuros contribuyentes)"
    ),
    ONE_TO_TWO(
        labelEs = "1 a 2 hijos",
        modifier = -0.05,
        invoiceReasonEs = "Bono por aporte de nuevos pagadores de impuestos"
    ),
    THREE_OR_MORE(
        labelEs = "3 o más hijos",
        modifier = -0.15,
        invoiceReasonEs = "Subsidio heroico de natalidad y expansión demográfica"
    )
}

enum class WorkSector(
    val labelEs: String,
    val modifier: Double,
    val invoiceReasonEs: String
) {
    PRIVATE_TECH(
        labelEs = "Sector Privado / Industria / Tecnología",
        modifier = -0.10,
        invoiceReasonEs = "Exención parcial por sostener el PIB real"
    ),
    PUBLIC_GOV(
        labelEs = "Sector Público / Administración Estatal",
        modifier = 0.25,
        invoiceReasonEs = "Recargo por eficiencia burocrática y nómina estatal"
    ),
    STUDENT_UNEMPLOYED(
        labelEs = "Estudiante / Sin empleo fijo",
        modifier = 0.05,
        invoiceReasonEs = "Tasa mínima de mantenimiento de infraestructura"
    ),
    CRYPTO_INFLUENCER(
        labelEs = "Creador de contenido / Crypto / Trading",
        modifier = 0.30,
        invoiceReasonEs = "Impuesto especial a la especulación y humo digital"
    )
}

enum class IdeologyAlignment(
    val modifier: Double,
    val invoiceReasonEs: String
) {
    LEFT_WING(
        modifier = 0.35,
        invoiceReasonEs = "Aporte solidario obligatorio (Cláusula de Justicia Social)"
    ),
    RIGHT_WING(
        modifier = -0.10,
        invoiceReasonEs = "Descuento por fe ciega en la mano invisible del mercado"
    ),
    NEUTRAL_PRAGMATIC(
        modifier = 0.05,
        invoiceReasonEs = "Recargo por tibieza ideológica"
    )
}
