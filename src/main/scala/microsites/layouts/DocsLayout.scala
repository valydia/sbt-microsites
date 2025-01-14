/*
 * Copyright 2016-2019 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package microsites.layouts

import microsites.MicrositeSettings

import scalatags.Text.TypedTag
import scalatags.Text.all._
import scalatags.Text.tags2.section

class DocsLayout(config: MicrositeSettings) extends Layout(config) {

  override def render: TypedTag[String] = {
    html(
      commonHead,
      body(cls := "docs", sideBarAndContent, scriptsDocs)
    )
  }

  def sideBarAndContent: TypedTag[String] = {

    val text = s"${config.identity.name} ${config.identity.description}"

    def githubLinks: Seq[TypedTag[String]] = {
      if (config.gitSettings.githubLinks) {
        Seq(
          li(
            id := "gh-eyes-item",
            cls := "hidden-xs",
            a(
              href := config.gitSiteUrl,
              i(cls := "fa fa-eye"),
              span("WATCH", span(id := "eyes", cls := "label label-default", "--")))),
          li(
            id := "gh-stars-item",
            cls := "hidden-xs",
            a(
              href := config.gitSiteUrl,
              i(cls := "fa fa-star-o"),
              span("STARS", span(id := "stars", cls := "label label-default", "--")))
          )
        )
      } else Seq.empty
    }

    def shareOnSocial: Seq[TypedTag[String]] = {
      if (config.visualSettings.shareOnSocial) {
        Seq(
          li(a(href := "#", onclick := s"shareSiteTwitter('$text');", i(cls := "fa fa-twitter"))),
          li(a(href := "#", onclick := s"shareSiteFacebook('$text');", i(cls := "fa fa-facebook"))),
          li(a(href := "#", onclick := "shareSiteGoogle();", i(cls := "fa fa-google-plus")))
        )
      } else Seq.empty
    }

    // format: off
    div(id := "wrapper",
      div(id := "sidebar-wrapper", buildSidebar),
      div(id := "page-content-wrapper",
        div(cls := "nav",
          div(cls := "container-fluid",
            div(cls := "row",
              div(cls := "col-lg-12",
                div(cls := "action-menu pull-left clearfix",
                  a(href := "#menu-toggle", id := "menu-toggle", i(cls := "fa fa-bars", aria.hidden := "true"))
                ),
                ul(cls := "pull-right",
                  githubLinks,
                  shareOnSocial
                )
              )
            )
          )
        ),
        div(id := "content", data("github-owner") := config.gitSettings.githubOwner, data("github-repo") := config.gitSettings.githubRepo,
          div(cls := "content-wrapper",
            section("{{ content }}"),
            editButton
          )
        )
      )
    )
    // format: on
  }

  def buildSidebar: TypedTag[String] = {
    // format: off
    ul(id := "sidebar", cls := "sidebar-nav",
      li(cls := "sidebar-brand",
        a(href := "{{ site.baseurl }}/", cls := "brand",
          div(cls := "brand-wrapper", span(config.identity.name))
        )
      ),
      "{% if site.data.menu.options %}",
        "{% assign items = site.data.menu.options %}",
        "{% for x in items %} ",
          "{% assign x_title = x.title | downcase %}",
          "{% assign page_title = page.title | downcase %}",
          "{% if x.menu_type.size == false or x.menu_type == page.section %}",
            li(
              a(href := "{{ site.baseurl }}/{{ x.url }}",
                cls := "{% if x_title == page_title %} active {% endif %}", "{{x.title}}"),
                "{% if x.nested_options %} ",
                  ul(
                    cls := "sub_section",
                    "{% for sub in x.nested_options %} ",
                    "{% assign sub_title = sub.title | downcase %}",
                    li(
                      a(href := "{{ site.baseurl }}/{{ sub.url }}",
                        cls := "{% if sub_title == page_title and x.section == sub.section %} active {% endif %}",
                        "{{sub.title}}"
                      )
                    ),
                    "{% endfor %}"
                  ),
                "{% endif %}"
            ),
          "{% endif %}",
        "{% endfor %}",
      "{% else %}",
        "{% assign items = site.pages | sort: 'weight' %}",
        "{% for x in items %}",
          "{% assign x_title = x.title | downcase %}",
          "{% assign page_title = page.title | downcase %}",
          "{% if x.section == page.section %}",
            li(
              a(
                href := "{{ site.baseurl }}{{x.url}}",
                cls := "{% if x_title == page_title %} active {% endif %}",
                "{{x.title}}"
              )
            ),
          "{% endif %}",
        "{% endfor %}",
      "{% endif %}"
    )
    // format: on
  }

  def scriptsDocs: List[TypedTag[String]] =
    scripts ++
      List(script(src := "{{ site.baseurl }}/js/main.js"))

  def editButton: Option[TypedTag[String]] =
    config.editButtonSettings.button match {
      case Some(button) =>
        Some(
          div(
            cls := "edit-button",
            a(
              href := config.gitSiteUrl,
              href := button.basePath,
              cls := "btn-sm btn-info",
              button.text
            )
          ))
      case _ => None
    }
}
