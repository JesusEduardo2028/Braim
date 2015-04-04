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
          requires :braim_token, type: String, desc: 'Auth token', documentation: { example: '837f6b854fc7802c2800302e' }
        end

        params :id do
          requires :id, type: String, desc: 'User ID', regexp: /^[[:xdigit:]]{24}$/
        end

        params :username do
          requires :username , type: String, desc: 'Username'
        end

      end

      before_validation do
        authenticated_user?
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
            use :auth
            use :pagination
          end
          get '/', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
            content_type "text/json"
            page = params[:page] || 1
            per_page = params[:per_page] || 10
            WillPaginate.per_page = per_page
            users = ::User.page(page)
            header 'total_pages', users.total_pages.to_s
            present users, with: Songbook::Entities::User
          end
          desc 'returns a user by username', {
            entity: Songbook::Entities::User,
            notes: <<-NOTES
              returns user information
            NOTES
          }
          params do
            use :auth
            use :username
          end
          get '/:username', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
            content_type "text/json"
            user = ::User.find_by(username: params[:username])
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
            use :auth
            use :username
            use :pagination
          end
          get '/:username/sessions', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
            content_type "text/json"

            user = ::User.find_by(username: params[:username])

            page = params[:page] || 1
            per_page = params[:per_page] || 10
            WillPaginate.per_page = per_page
            sessions =  user.emo_sessions.page(page)
            header 'total_pages', sessions.total_pages.to_s
            present sessions, with:  Songbook::Entities::Session
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
            use :auth
            use :pagination
            use :username
          end
          get '/:username/all_emo_entries', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
            content_type "text/json"
            user = ::User.find_by(username: params[:username])
            page = params[:page] || 1
            per_page = params[:per_page] || 10
            WillPaginate.per_page = per_page
            entries = user.all_emo_entries.page(page)
            header 'total_pages', entries.total_pages.to_s
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
            use :auth
            use :pagination
            use :username
          end
          get '/:username/all_player_entries', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
            content_type "text/json"
            user = ::User.find_by(username: params[:username])
            page = params[:page] || 1
            per_page = params[:per_page] || 10
            WillPaginate.per_page = per_page
            entries = user.all_player_entries.page(page)
            header 'total_pages', entries.total_pages.to_s
            present entries , with:  Songbook::Entities::PlayerEntry
          end
=begin
          desc 'returns all fragments for a song', {
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
            use :auth
            use :pagination
            use :username
            requires :song_id , type: String, desc: 'Song id'
          end
          get '/:username/all_song_fragments/:song_id', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
            content_type "text/json"
            user = ::User.find_by(username: params[:username])
            page = params[:page] || 1
            per_page = params[:per_page] || 10
            WillPaginate.per_page = per_page
            entries = user.all_song_fragments(params[:song_id])
            #binding.pry
            #header 'total_pages', entries.total_pages.to_s
            present entries , with:  Songbook::Entities::SongFragment
          end
=end
        end
      end
    end
  end
end
