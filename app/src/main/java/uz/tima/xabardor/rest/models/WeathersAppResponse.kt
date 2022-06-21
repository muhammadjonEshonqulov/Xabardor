package uz.tima.xabardor.rest.models

import Andijan
import Bukhara
import Fergana
import Gulistan
import Jizzakh
import Karshi
import Namangan
import Navoiy
import Nukus
import Samarkand
import Tashkent
import Termez
import Urgench
import com.google.gson.annotations.SerializedName

data class WeathersAppResponse (

	@SerializedName("Tashkent")
	val tashkent : Tashkent,
	@SerializedName("Andijan")
	val andijan : Andijan,
	@SerializedName("Bukhara")
	val bukhara : Bukhara,
	@SerializedName("Fergana")
	val fergana : Fergana,
	@SerializedName("Jizzakh")
	val jizzakh : Jizzakh,
	@SerializedName("Namangan")
	val namangan : Namangan,
	@SerializedName("Navoiy")
	val navoiy : Navoiy,
	@SerializedName("Karshi")
	val karshi : Karshi,
	@SerializedName("Samarkand")
	val samarkand : Samarkand,
	@SerializedName("Gulistan")
	val gulistan : Gulistan,
	@SerializedName("Termez")
	val termez : Termez,
	@SerializedName("Urgench")
	val urgench : Urgench,
	@SerializedName("Nukus")
	val nukus : Nukus
)