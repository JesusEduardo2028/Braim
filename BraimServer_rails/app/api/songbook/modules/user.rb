module Songbook
  module Modules
    class User < Grape::API
      
      format :json

      content_type :json, 'application/json'

      helpers do
        # Pagination stuff, unfortunately it cannot be located in API root class
        params :pagination do
          optional :page, type: Integer
          optional :per_page, type: Integer
        end

        params :auth do
          requires :songbook_token, type: String, desc: 'Auth token', documentation: { example: '837f6b854fc7802c2800302e' }
        end

        params :id do
          requires :id, type: String, desc: 'User ID', regexp: /^[[:xdigit:]]{24}$/
        end

      end

      before_validation do
        #authenticated_user?
      end

      version :v1 do
        resource :users do
          desc 'returns all existent users', {
            entity: Songbook::Entities::User,
            notes: <<-NOTES
              ### All paginated users
            NOTES
          }
          params do
            #use :auth
            use :pagination
          end
          get '/', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
            content_type "text/json"
            page = params[:page] || 1
            per_page = params[:per_page] || 10
            WillPaginate.per_page = per_page
            users = ::User.page(page)
            present users, with: Songbook::Entities::User
          end
          desc 'returns a user', {
            entity: Songbook::Entities::User,
            notes: <<-NOTES
              returns user information
            NOTES
          }
          params do
            #use :auth
            use :id
          end
          get '/:id', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
            content_type "text/json"
            user = ::User.find(params[:id])
            present user, with:  Songbook::Entities::User
          end
          desc 'returns all sessions from a user', {
            entity: Songbook::Entities::Session,
            notes: <<-NOTES
               ### Description
                It returns all sessions from a user

                ### Example successful response
                    [
                      {
                        "id":"548f1e03f080c0b76fffd507",
                        "start_at":1418665475068.0,
                        "user_id":"548f1ddd6a65737080000000"
                      },
                      {
                        "id":"548f25b2f080c0b76fffd5a9",
                        "start_at":1418667442636.0,
                        "user_id":"548f1ddd6a65737080000000"
                      }
                    ]

            NOTES
          }
          params do
            #use :auth
            use :id
          end
          get '/:id/sessions', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
            content_type "text/json"
            user = ::User.find(params[:id])

            present user.emo_sessions, with:  Songbook::Entities::Session
          end
          desc 'returns all emotional entries from a given user', {
            entity: Songbook::Entities::Session,
            notes: <<-NOTES
               ### Description
                CAUTIONNN THIS MAY TAKE TIME!!!!!
                It returns all emotional entries from a user

                ### Example successful response
                    [
                      {
                        emo entry item
                      },
                        emo entry item
                      {
                    ]
            NOTES
          }
          params do
            #use :auth
            use :pagination
            use :id
          end
          get '/:id/all_emo_entries', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
            content_type "text/json"
            user = ::User.find(params[:id])
            page = params[:page] || 1
            per_page = params[:per_page] || 10
            WillPaginate.per_page = per_page
            entries = user.all_emo_entries
            present entries, with:  Songbook::Entities::EmoEntry
          end
          desc 'returns all player entries from a given user', {
            entity: Songbook::Entities::Session,
            notes: <<-NOTES
               ### Description
                CAUTIONNN THIS MAY TAKE TIME!!!!!
                It returns all player entries from a session

                ### Example successful response
                    [
                      {
                        player entry item
                      },
                        player entry item
                      {
                    ]
            NOTES
          }
          params do
            #use :auth
            use :pagination
            use :id
          end
          get '/:id/all_player_entries', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
            content_type "text/json"
            user = ::User.find(params[:id])
            page = params[:page] || 1
            per_page = params[:per_page] || 10
            WillPaginate.per_page = per_page
            entries = user.all_player_entries
            present entries , with:  Songbook::Entities::PlayerEntry
          end
        end
      end
    end
  end
end
