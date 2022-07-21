package ge.gmegrelishvili.messengerapp.model.repository.strategy

class MillisTimeStrategy : TimeStrategy {

    override fun getTimeValue(): Long {
        return System.currentTimeMillis()
    }

}