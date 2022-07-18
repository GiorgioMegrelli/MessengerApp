package ge.gmegrelishvili.messengerapp.model.repository.strategy

interface EmailStrategy {

    fun createEmail(name: String): String

}