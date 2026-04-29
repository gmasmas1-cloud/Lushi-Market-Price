package com.example.ecolush.data.model

data class Commune(
    val name: String,
    val quartiers: List<String>
)

object LocationData {
    val communes = listOf(
        Commune(
            "Annexe",
            listOf("Kalebuka", "Kasapa", "Kasungami", "Kimbembe", "Kisanga", "Luwowoshi", "Munua", "Naviundu", "Kamasaka", "Kamisepe", "Kamatete", "Don Bosco", "Espoir", "Kilobelobe")
        ),
        Commune(
            "Kamalondo",
            listOf("Babemba", "Kitumaïni")
        ),
        Commune(
            "Kampemba",
            listOf("Bel-air I", "Bel-air II", "Bongonga", "Industriel", "Kafubu", "Kinka-ville", "Kampemba", "Kigoma", "Hewa-bora")
        ),
        Commune(
            "Katuba",
            listOf("Bukama", "Kaponda", "Kinyama", "Kimilolo", "Kisale", "Lufira", "Musumba", "Mwana Shaba", "Nsele", "Upemba", "Katuba 2", "Katuba 3")
        ),
        Commune(
            "Kenya",
            listOf("Brondo", "Lualaba", "Luapula", "Luvua")
        ),
        Commune(
            "Lubumbashi",
            listOf("Gambela", "Gambela II", "Kalubwe", "Kiwele", "Lido golf", "Lumumba", "Golf Malela", "Golf Munua", "Golf Kabulameshi", "Makutano", "Mampala")
        ),
        Commune(
            "Ruashi",
            listOf("Bendera", "Congo", "Kalukuluku", "Kawama", "Luano", "Matoleo", "Shindaika", "Gécamines", "Kasapa", "Mobutu")
        )
    )
}
