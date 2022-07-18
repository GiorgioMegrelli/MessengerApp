package ge.gmegrelishvili.messengerapp.model.repository.strategy

class TestEmailStrategy : EmailStrategy {

    override fun createEmail(name: String): String {
        return "${name}@${EmailDomain}"
    }

    companion object {
        const val EmailDomain = "test.ge"
    }

}