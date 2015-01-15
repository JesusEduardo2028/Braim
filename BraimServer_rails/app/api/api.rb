class API < Grape::API
  prefix 'api'

  helpers do
    # Authentication stuff
    def warden
      env['warden']
    end

    def authenticated
      if warden.authenticated?
        return true
      elsif params[:songbook_token] and
          AccessGrant.find_access(params[:songbook_token])
        return true
      else
        error!('401 Unauthorized', 401)
      end
    end

    def current_user
      warden.user || AccessGrant.find_access(params[:songbook_token]).try(:user)
    end

    # returns 401 if there's no current user
    def authenticated_user?
      authenticated
      error!('401 Unauthorized', 401) unless current_user
    end
  end

  # Separate the api into smaller
  # modules like this
  mount Songbook::Modules::Ping
  mount Songbook::Modules::Token
  mount Songbook::Modules::Band
  mount Songbook::Modules::User
  mount Songbook::Modules::Song
  # Adds the swagger documentation to your
  # api. You only need this once, not in
  # every sub module
  add_swagger_documentation(
    base_path: "/",
    hide_documentation_path: true,
    api_version: 'v1',
    markdown: true
  )
end
