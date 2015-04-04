describe 'Band', :type => :request do

  describe :v1 do

    context 'users', :keep_db do

      before :context do
        @user = FactoryGirl.create :user, email: 'allam.britto@fake.com', password: '12345678', password_confirmation: '12345678'
        FactoryGirl.create_list(:user, 20)
        User.all.each do|user|
          FactoryGirl.create_list(:emo_session, 10 , user: user);
        end
        @user.emo_sessions.each do|session|
          FactoryGirl.create_list(:emo_entry, 10 , user_id: @user.id, emo_session: session);
          FactoryGirl.create_list(:player_entry, 10 , user_id: @user.id, emo_session: session);
        end
        get '/api/v1/token', %Q{data=allam.britto@fake.com&password=12345678}
        @credentials = JSON.parse response.body
      end

      context 'GET' do

        it 'verifies that response has the elements number specified in per_page param' do
          token = "braim_token=#{@credentials['access_token']}"
          per_page = 5
          data = "per_page=#{per_page}"

          get '/api/v1/users', "#{token}&#{data}"

          expect(response.status).to eq 200
          expect(JSON.parse(response.body).count).to be per_page
        end

        context '/:username' do

          it 'gets user by username' do

            user = User.last

            expected_response = {
              id: user.id.to_s,
              username: user.username,
              email: user.email
            }

            get "/api/v1/users/#{user.username}", braim_token: @credentials['access_token']
            expect(response.status).to eq 200
            expect(JSON.parse(response.body)).to match expected_response.stringify_keys
          end

        end

        context "/:username/sessions" do
          it 'gets all paginated sessions for a user' do

            user = User.first
            token = "braim_token=#{@credentials['access_token']}"
            per_page = 10
            data = "per_page=#{per_page}"

            get "/api/v1/users/#{user.username}/sessions", "#{token}&#{data}"

            expect(response.status).to eq 200
            expect(JSON.parse(response.body).count).to be per_page
          end
        end

        context "/:username/all_emo_entries" do
          it 'gets all paginated emo entries for a user' do

            token = "braim_token=#{@credentials['access_token']}"
            per_page = 5
            data = "per_page=#{per_page}"

            get "/api/v1/users/#{@user.username}/all_emo_entries", "#{token}&#{data}"

            expect(response.status).to eq 200
            expect(JSON.parse(response.body).count).to be per_page
          end
        end

        context "/:username/all_player_entries" do
          it 'gets all paginated player entries for a user' do

            token = "braim_token=#{@credentials['access_token']}"
            per_page = 5
            data = "per_page=#{per_page}"
            
            get "/api/v1/users/#{@user.username}/all_player_entries", "#{token}&#{data}"

            expect(response.status).to eq 200
            expect(JSON.parse(response.body).count).to be per_page
          end
        end
      end
    end
  end
end
