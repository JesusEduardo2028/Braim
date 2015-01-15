module Songbook
  module Modules
    class Ping < Grape::API
      format :json

      version :v1 do

        desc 'Returns pong.'
        params do
          optional :pong, type: String, desc: 'pong value'
        end
        get :ping, http_codes: [ [200, "Successful"] ] do
          { ping: params[:pong] || 'pong' }
        end

        desc 'returns pong if authentication process is valid.'
        params do
          requires :songbook_token, type: String, desc: 'Auth token'
          optional :pong, type: String, desc: 'pong value'
        end
        get :protected_ping, http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
          authenticated_user?
          { ping: params[:pong] || 'pong' }
        end

      end
    end
  end
end
