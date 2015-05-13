module Songbook
  module Modules
    class BraimSession < Grape::API
      
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

      end

      before_validation do
        authenticated_user?
      end

      version :v1 do
        resource :braim_sessions do
          desc 'returns all existent braim sessions', {
            entity: Songbook::Entities::BraimSession,
            notes: <<-NOTES
              Returns all existent sessions paginated
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
            braim_sessions = ::BraimSession.page(page)
            header 'total_pages', braim_sessions.total_pages.to_s
            present braim_sessions, with: Songbook::Entities::BraimSession
          end
          desc 'returns one existent braim session by :id', {
            entity: Songbook::Entities::BraimSession,
            notes: <<-NOTES
              Returns one existent braim session by :id
            NOTES
          }
          params do
            use :auth
            use :id
          end
          get '/:id', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
            content_type "text/json"
            braim_session = ::BraimSession.find(params[:id])
            present braim_session, with: Songbook::Entities::BraimSession
          end
          desc 'returns all epoc entries from a given braim session', {
            entity: Songbook::Entities::EpocEntry,
            notes: <<-NOTES
               ### Description
                It returns all epoc entries from a session

                ### Example successful response
                    [
                      {
                        epoc entry item
                      },
                        epoc entry item
                      {
                    ]
            NOTES
          }
          params do
            use :auth
            use :id
            use :pagination
          end
          get '/:id/epoc_entries', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
            content_type "text/json"
            page = params[:page] || 1
            per_page = params[:per_page] || 10
            WillPaginate.per_page = per_page
            braim_session = ::BraimSession.find(params[:id])
            epoc_entries = braim_session.epoc_entries.page(page)
            header 'total_pages', epoc_entries.total_pages.to_s
            present epoc_entries, with:  Songbook::Entities::EpocEntry
          end
          desc 'returns all player entries from a given braim session', {
            entity: Songbook::Entities::PlayerEntry,
            notes: <<-NOTES
               ### Description
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
            use :id
            use :pagination
          end
          get '/:id/player_entries', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
            content_type "text/json"
            page = params[:page] || 1
            per_page = params[:per_page] || 10
            WillPaginate.per_page = per_page
            braim_session = ::BraimSession.find(params[:id])
            player_entries = braim_session.player_entries.page(page)
            header 'total_pages', player_entries.total_pages.to_s
            present player_entries, with:  Songbook::Entities::PlayerEntry
          end

          # POST
          desc 'creates a new braim session', {
              entity: Songbook::Entities::Band,
              notes: <<-NOTE
                ### Description
                It creates a new session and returns its current representation.

                ### Example successful response

                    {
                      "id": "137a66834fb7802c280000ef",
                      

                    }

              NOTE
            }
          params do
            use :auth
            optional :braim_session, type: Hash do
              requires :user_id, type: String, desc: 'User id of user that create the session', documentation: { example: 'Rock' }
            end
          end
          post '/', http_codes: [
            [200, "Successful"],
            [400, "Invalid parameter in entry"],
            [401, "Unauthorized"],
            [404, "Entry not found"],
          ]  do
            content_type "text/json"
            braim_session_params = params[:braim_session]
            user = ::User.find(braim_session_params['user_id'])
            braim_session = user.braim_sessions.build
            if braim_session.save
              present braim_session, with: Songbook::Entities::BraimSession
            else
              error!(entry.errors, 400)
            end
          end


        end
      end
    end
  end
end
