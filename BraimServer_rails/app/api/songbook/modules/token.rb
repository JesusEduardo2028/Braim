module Songbook
  module Modules
    class Token < Grape::API
      format :json

      version :v1 do
        desc 'Returns a token by authenticating user email and password credentials', {
            entity: Songbook::Entities::Token,
            notes: <<-NOTE
              ### Description
              It creates a new entry record and returns its current representation.

              ### Example successful response

                  {
                    "access_token": "the_most_secure_token",
                    "expires_in": "2014-06-09T13:50:52-05:00"
                  }
            NOTE
          }
        params do
          requires :data, type: String, desc: 'Username or Email'
          requires :password, type: String, desc: 'User Password'
        end
        get 'token', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
          user = ::User.authenticate(params[:data], params[:password])
          error!("401 Unauthorized", 401) if user.nil?
          AccessGrant.prune!
          access_grant = AccessGrant.find_or_create_by(:user => user)
          {
            access_token: access_grant.access_token,
            expires_in: access_grant.access_token_expires_at,
            user_logged: user.username
          }
        end
      end
    end
  end
end
