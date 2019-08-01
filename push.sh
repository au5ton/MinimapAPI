#!/bin/sh

# https://gist.github.com/willprice/e07efd73fb7f13f917ea
# https://gist.github.com/fernandezpablo85/03cf8b0cd2e7d8527063

setup_git() {
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "Travis CI"
}

commit_website_files() {
  git checkout -b gh-pages
  git add maven/
  git commit --message "Travis build: $TRAVIS_BUILD_NUMBER"
}

upload_files() {
  git remote add origin-pages https://au5ton:${GH_TOKEN}@github.com/au5ton/MinimapAPI.git > /dev/null 2>&1
  # overwrite current "gh-pages" branch
  git push -f --quiet --set-upstream origin-pages gh-pages 
}

setup_git
commit_website_files
upload_files

