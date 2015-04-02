describe 'Session', :type => :request do

  describe :v1 do

    context 'sessions', :keep_db do

      before :context do
        @user = FactoryGirl.create :user, email: 'allam.britto@fake.com', password: '12345678', password_confirmation: '12345678'
        FactoryGirl.create_list(:emo_session, 20,user: @user)
        get '/api/v1/token', %Q{data=allam.britto@fake.com&password=12345678}
        @credentials = JSON.parse response.body
      end

      context 'GET' do

        it 'verifies that response has the elements number specified in per_page param' do
          token = "braim_token=#{@credentials['access_token']}"
          per_page = 5
          data = "per_page=#{per_page}"

          get '/api/v1/sessions', "#{token}&#{data}"

          expect(response.status).to eq 200
          expect(JSON.parse(response.body).count).to be per_page
        end

        context '/:id' do

          it 'gets session by id' do

            session = EmoSession.last

            expected_response = {
              id: session.id.to_s,
              start_at: session.start_at,
              user_id: session.user_id
            }

            get "/api/v1/sessions/#{session.id.to_s}", braim_token: @credentials['access_token']
            expect(response.status).to eq 200
            expect(JSON.parse(response.body)).to match expected_response.stringify_keys
          end

        end

        context "/:id/emo_entries" do
          it 'gets all paginated emo entries for a session' do

            session = EmoSession.first
            FactoryGirl.create_list(:emo_entry, 10 , user_id: @user.id.to_s, emo_session: session);
            
            token = "braim_token=#{@credentials['access_token']}"
            per_page = 5
            data = "per_page=#{per_page}"

            get "/api/v1/sessions/#{session.id.to_s}/emo_entries", "#{token}&#{data}"

            expect(response.status).to eq 200
            expect(JSON.parse(response.body).count).to be per_page
          end
        end

        context "/:id/player_entries" do
          it 'gets all paginated player entries for a session' do

            session = EmoSession.first
            FactoryGirl.create_list(:player_entry, 10 , user_id: @user.id.to_s, emo_session: session);
            
            token = "braim_token=#{@credentials['access_token']}"
            per_page = 5
            data = "per_page=#{per_page}"

            get "/api/v1/sessions/#{session.id.to_s}/player_entries", "#{token}&#{data}"

            expect(response.status).to eq 200
            expect(JSON.parse(response.body).count).to be per_page
          end
        end

      end

      context 'POST' do
        it 'returns a representation of new session and code 201' do
          new_session = @user.emo_sessions.build start_at: Time.now

          token = "braim_token=#{@credentials['access_token']}"
          data = "session[user_id]=#{new_session.user_id}"

          expected_response = {
            user_id: new_session.user_id.to_s
          }

          post '/api/v1/sessions', "#{token}&#{data}"

          expect(response.status).to eq 201
          expect(JSON.parse(response.body).except('id','start_at')).to match(expected_response.stringify_keys)
        end
      end

    end

  end

end