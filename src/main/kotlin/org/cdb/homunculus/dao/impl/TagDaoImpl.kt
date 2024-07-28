package org.cdb.homunculus.dao.impl

import com.mongodb.client.model.Filters
import kotlinx.coroutines.flow.Flow
import org.cdb.homunculus.annotations.Index
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.dao.TagDao
import org.cdb.homunculus.models.embed.Tag
import org.cdb.homunculus.utils.StringNormalizer
import java.util.regex.Pattern

class TagDaoImpl(client: DBClient) : TagDao(client) {
	@Index(name = "by_fuzzy_name", property = "normalizedName", unique = false)
	override fun getByFuzzyName(query: String): Flow<Tag> =
		collection.find(
			Filters.and(
				listOfNotNull(
					Filters.regex(Tag::normalizedName.name, Pattern.compile("^${StringNormalizer.normalize(query)}.*")),
				),
			),
		)
}
