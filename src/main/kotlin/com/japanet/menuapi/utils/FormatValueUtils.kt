package com.japanet.menuapi.utils

import com.japanet.menuapi.exception.InvalidPriceException
import java.math.BigDecimal

class FormatValueUtils {

    companion object {

        fun convertPrice(price: String?): BigDecimal? = price?.let {
            BigDecimal(price).run {
                if (this.precision() > 9 || this.scale() > 2) throw InvalidPriceException("Price:[$this] must be of max scale 2 and max precision 9")
                this
            }
        }

    }
}
