package com.mp08.ac02taulaperidica

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ElementsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        // Find all the views of the list item
        private val mRepositoryName: TextView = itemView.findViewById(R.id.tv_repo_name)
        private val mRepositoryDescription: TextView = itemView.findViewById(R.id.tv_repo_description)
        private val mRepositoryStarCount: TextView = itemView.findViewById(R.id.tv_star_count)
        private val mRepositoryLanguage: TextView = itemView.findViewById(R.id.tv_language)
        private val mRepositoryUpdatedAt: TextView = itemView.findViewById(R.id.tv_updated_at)
        private val mRepositoryLicense: TextView = itemView.findViewById(R.id.tv_license)

        // Show the data in the views
        fun bind(repo: GithubRepository) {
            val name = repo.name
            val description = repo.description
            val stargazersCount = repo.stargazersCount
            val language = repo.language
            val updatedAt = repo.updatedAt
            val license = repo.license
            mRepositoryName.text = name
            mRepositoryStarCount.text = stargazersCount.toString()
            mRepositoryUpdatedAt.text = updatedAt

            // Since the data in these can be null we check and bind data
            // or remove the view otherwise
            bindOrHideTextView(mRepositoryDescription, description)
            bindOrHideTextView(mRepositoryLanguage, language)
            bindOrHideTextView(mRepositoryLicense, license)
        }

        private fun bindOrHideTextView(textView: TextView, data: String?) {
            if (data == null) {
                textView.visibility = View.GONE
            } else {
                textView.text = data
                textView.visibility = View.VISIBLE
            }
        }
    }